package org.cryptoBros.persistence.SQL;

import com.google.gson.Gson;
import org.cryptoBros.business.Workers.Bot;
import org.cryptoBros.business.Crypto;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.persistence.AtomicPersistence;
import org.cryptoBros.persistence.Exceptions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL implementation of AtomicPersistence using PostgreSQL transactions.
 */
public class AtomicSQL implements AtomicPersistence {

    /** Creates a new AtomicSQL instance. */
    public AtomicSQL() {}

    private static final String CRYPTO_FILEPATH = "src/main/java/org/cryptoBros/crypto.json";

    private static final String INSERT_USER = """
        INSERT INTO Users (username, email, password)
        VALUES (?, ?, ?)
        """;

    private static final String CHECK_CRYPTO_EXISTS = """
    SELECT 1
    FROM Cryptocurrency
    WHERE symbol = ?
    """;

    private static final String INSERT_CRYPTO = """
        INSERT INTO Cryptocurrency (symbol, name, current_price, original_price, volatility)
        VALUES (?, ?, ?, ?, ?)
        ON CONFLICT (symbol) DO NOTHING
        """;

    private static final String INSERT_BOT = """
        INSERT INTO Bots (bot_id, crypto_id, volatility)
        VALUES (?, ?, ?)
        """;

    private static final String SELECT_BOT_ID = """
    SELECT bot_id FROM Bots b 
    JOIN Cryptocurrency c ON c.symbol = b.crypto_id 
   WHERE c.name = ?
    """;

    private static final String SELECT_CURRENT_PRICE = """
    SELECT current_price FROM Cryptocurrency WHERE name = ?
    """;

    private static final String SELECT_HOLDERS = """
	SELECT p.user_id, p.units FROM Portfolio p
    JOIN Cryptocurrency c ON c.symbol = p.crypto_id
    WHERE c.name = ?
    """;

    private static final String ADJUST_BALANCE = """
    UPDATE Users SET balance = balance + ? WHERE user_id = ?
    """;

    private static final String DELETE_CRYPTO = """
    DELETE FROM Cryptocurrency WHERE name = ?
    """;

    private static final String DELETE_USER = """
    DELETE FROM Users WHERE user_id = ?
    """;

    private static final String SELECT_EXISTING_BOTS = """
    SELECT bot_id, crypto_id, volatility
    FROM Bots
    """;

    private static final String HAS_CRYPTOS = """
    SELECT 1
    FROM Cryptocurrency
    LIMIT 1
    """;

    private static final String INSERT_NOTIFICATION = """
        INSERT INTO Notifications (user_id, message)
        VALUES(?,?)
    """;

    private static final String POP_NOTIFICATION = """
        DELETE FROM Notifications
        WHERE user_id = ?
        RETURNING message
    """;

    /**
     * Atomically creates a cryptocurrency and its associated bot user within a single transaction.
     * Inserts a bot user row, the crypto row, and the bot mapping row; rolls back all changes if any step fails.
     *
     * @param crypto the cryptocurrency to create
     * @return the generated bot user ID
     * @throws DbConnectionException  if the database connection or transaction fails
     * @throws BotGenerationException  if the bot user or bot mapping could not be inserted
     * @throws CryptoNotAddedException if the crypto row could not be inserted
     */
    @Override
    public long createCryptoWithBot(Crypto crypto)
            throws DbConnectionException, BotGenerationException, CryptoNotAddedException {

        try (Connection conn = DbConnectionSingleton.getInstance().connect()) {
            conn.setAutoCommit(false);

            try {
                long botUserId = insertUser(conn, crypto.getSymbol());
                insertCrypto(conn, crypto);
                insertBot(conn, botUserId, crypto.getSymbol(), crypto.getVolatility());

                conn.commit();
                return botUserId;

            } catch (Exception e) {
                conn.rollback();   // nothing persisted if any step fails
                if (e instanceof BotGenerationException botGenerationException) throw botGenerationException;
                if (e instanceof CryptoNotAddedException cryptoNotAddedException) throw cryptoNotAddedException;
                if (e instanceof SQLException sqlException)
                    throw new DbConnectionException("DB transaction error: " + sqlException.getMessage());
                throw new DbConnectionException("Unexpected transaction error: " + e.getMessage());
            }

        } catch (SQLException e) {
            throw new DbConnectionException("DB connection error: " + e.getMessage());
        }
    }

    private long insertUser(Connection conn, String symbol)
            throws SQLException, BotGenerationException {

        try (PreparedStatement ps = conn.prepareStatement(
                INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, "BOT_" + symbol);
            ps.setString(2, "bot." + symbol + "@system.cryptobros.internal");
            ps.setString(3, "");   // bots never log in

            if (ps.executeUpdate() == 0)
                throw new BotGenerationException("Failed to insert bot user for " + symbol);

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next())
                    throw new BotGenerationException("No user_id generated for bot " + symbol);
                return keys.getLong(1);
            }
        }
    }

    private void insertCrypto(Connection conn, Crypto crypto)
            throws SQLException, CryptoNotAddedException {

        try (PreparedStatement ps = conn.prepareStatement(INSERT_CRYPTO)) {
            ps.setString(1, crypto.getSymbol());
            ps.setString(2, crypto.getName());
            ps.setDouble(3, crypto.getCurrentPrice());
            ps.setDouble(4, crypto.getInitialPrice());
            ps.setDouble(5, crypto.getVolatility());

            if (ps.executeUpdate() == 0)
                throw new CryptoNotAddedException(
                        "Crypto '" + crypto.getSymbol() + "' already exists or could not be added.");
        }
    }

    private void insertBot(Connection conn, long botUserId, String symbol, double volatility)
            throws SQLException, BotGenerationException {

        try (PreparedStatement ps = conn.prepareStatement(INSERT_BOT)) {
            ps.setLong  (1, botUserId);
            ps.setString(2, symbol);
            ps.setDouble(3, volatility);

            if (ps.executeUpdate() == 0)
                throw new BotGenerationException("Failed to insert bot for " + symbol);
        }
    }

    /**
     * Atomically deletes a cryptocurrency and its bot within a single transaction.
     * Refunds all non-bot holders by crediting their balance with currentPrice times units held,
     * inserts a notification for each refunded user, cascade-deletes the crypto row, and removes the bot user.
     *
     * @param name the name of the cryptocurrency to delete
     * @return a map of user IDs to refund amounts for each refunded holder
     * @throws DbConnectionException  if the database connection or transaction fails
     * @throws CryptoNotFoundException if the cryptocurrency or its bot is not found
     */
    @Override
    public Map<Long, Double> deleteCryptoWithBot(String name)
            throws DbConnectionException, CryptoNotFoundException {

        Map<Long, Double> refunds = new HashMap<>();

        try (Connection conn = DbConnectionSingleton.getInstance().connect()) {
            conn.setAutoCommit(false);

            try {
                long botUserId = fetchBotUserId(conn, name);
                double currentPrice = fetchCurrentPrice(conn, name);

                // Fetch all holders and compute refunds (skip the bot user)
                try (PreparedStatement ps = conn.prepareStatement(SELECT_HOLDERS)) {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        long userId = rs.getLong("user_id");
                        double units = rs.getDouble("units");
                        if (userId != botUserId) {
                            refunds.put(userId, currentPrice * units);
                        }
                    }
                }

                // Credit each holder's balance
                for (Map.Entry<Long, Double> entry : refunds.entrySet()) {
                    try (PreparedStatement ps = conn.prepareStatement(ADJUST_BALANCE)) {
                        ps.setDouble(1, entry.getValue());
                        ps.setLong(2, entry.getKey());
                        ps.executeUpdate();
                    }
                }

                for (long userId: refunds.keySet()) {
                    try (PreparedStatement ps = conn.prepareStatement(INSERT_NOTIFICATION)){
                        ps.setLong(1, userId);
                        ps.setString(2, "The cryptocurrency '" + name + "' has been removed from the system. "
                                + "Your balance has been refunded accordingly.");
                        ps.executeUpdate();
                    }
                }

                // Delete crypto (cascades Bots, Portfolio, Crypto_History)
                try (PreparedStatement ps = conn.prepareStatement(DELETE_CRYPTO)) {
                    ps.setString(1, name);
                    if (ps.executeUpdate() == 0)
                        throw new CryptoNotFoundException(
                                "Crypto '" + name + "' not found.");
                }

                // Delete the bot user (Bots row already gone via cascade)
                try (PreparedStatement ps = conn.prepareStatement(DELETE_USER)) {
                    ps.setLong(1, botUserId);
                    ps.executeUpdate();
                }

                conn.commit();
                return refunds;

            } catch (Exception e) {
                conn.rollback();
                if (e instanceof CryptoNotFoundException cryptoNotFoundException) throw cryptoNotFoundException;
                if (e instanceof SQLException sqlException)
                    throw new DbConnectionException("DB transaction error: " + sqlException.getMessage());
                throw new DbConnectionException("Unexpected transaction error: " + e.getMessage());
            }

        } catch (SQLException e) {
            throw new DbConnectionException("DB connection error: " + e.getMessage());
        }
    }

    private double fetchCurrentPrice(Connection conn, String symbol)
            throws SQLException, CryptoNotFoundException {

        try (PreparedStatement ps = conn.prepareStatement(SELECT_CURRENT_PRICE)) {
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                throw new CryptoNotFoundException(
                        "Crypto '" + symbol + "' not found.");
            return rs.getDouble("current_price");
        }
    }

    private long fetchBotUserId(Connection conn, String symbol)
            throws SQLException, CryptoNotFoundException {

        try (PreparedStatement ps = conn.prepareStatement(SELECT_BOT_ID)) {
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                throw new CryptoNotFoundException(
                        "No bot found for crypto '" + symbol + "'.");
            return rs.getLong("bot_id");
        }
    }

    private boolean cryptoExists(Connection conn, String symbol) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(CHECK_CRYPTO_EXISTS)) {
            ps.setString(1, symbol);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private List<Bot> loadExistingBots(Connection conn, CryptoManager cryptoManager) throws SQLException {
        List<Bot> bots = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(SELECT_EXISTING_BOTS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                bots.add(new Bot(
                        rs.getLong("bot_id"),
                        rs.getString("crypto_id"),
                        rs.getDouble("volatility"),
                        cryptoManager
                ));
            }
        }

        return bots;
    }

    /**
     * Loads initial cryptocurrency data from the JSON seed file and creates missing cryptos with bots.
     * If the database contains no cryptocurrencies, each entry from the seed file is inserted via
     * {@link #createCryptoWithBot(Crypto)}. Existing bots are then loaded and returned.
     *
     * @param cryptoManager the crypto manager used to wire bot callbacks
     * @param seedFromJson  whether the database is empty and needs seeding from JSON
     * @return a list of bots loaded from the database
     * @throws CryptoNotAddedException if a crypto entry could not be inserted
     * @throws DbConnectionException   if the database connection fails
     * @throws FileNotFoundException   if the crypto seed JSON file is not found
     * @throws BotGenerationException  if a bot user could not be created
     */
    @Override
    public List<Bot> loadInitialData(CryptoManager cryptoManager, boolean seedFromJson)
            throws CryptoNotAddedException, DbConnectionException, FileNotFoundException, BotGenerationException {

        Gson gson = new Gson();

        try (Connection conn = DbConnectionSingleton.getInstance().connect();
             FileReader fileReader = new FileReader(CRYPTO_FILEPATH)) {
            Crypto[] cryptos = gson.fromJson(fileReader, Crypto[].class);

            if (!hasCryptos(conn)) {
                for (Crypto crypto : cryptos) {
                    if (!cryptoExists(conn, crypto.getSymbol())) {
                        createCryptoWithBot(crypto);
                    }
                }
            }

                return loadExistingBots(conn, cryptoManager);

        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw new DbConnectionException("Error while reading initial crypto data: " + e.getMessage());
        } catch (SQLException e) {
            throw new DbConnectionException("DB error while loading initial data: " + e.getMessage());
        }
    }

    /**
     * Retrieves and deletes all pending notifications for a user in a single SQL statement.
     * Uses DELETE ... RETURNING to atomically fetch and remove the notification rows.
     *
     * @param userId the ID of the user whose notifications to pop
     * @return a list of notification messages, empty if none exist
     * @throws DbConnectionException if the database connection fails
     */
    @Override
    public List<String> popNotifications(long userId) throws DbConnectionException {
        List<String> messages = new ArrayList<>();
        try (Connection conn = DbConnectionSingleton.getInstance().connect();
             PreparedStatement ps = conn.prepareStatement(POP_NOTIFICATION)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(rs.getString("message"));
            }
            return messages;
        }catch (SQLException e) {
            throw new DbConnectionException("Error fetching notifications: " + e.getMessage());
        }
    }

    private boolean hasCryptos(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(HAS_CRYPTOS);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
