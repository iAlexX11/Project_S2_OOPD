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

public class AtomicSQL implements AtomicPersistence {

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
    SELECT bot_id FROM Bots WHERE crypto_id = ?
    """;

    private static final String SELECT_CURRENT_PRICE = """
    SELECT current_price FROM Cryptocurrency WHERE symbol = ?
    """;

    private static final String SELECT_HOLDERS = """
    SELECT user_id, units FROM Portfolio WHERE crypto_id = ?
    """;

    private static final String ADJUST_BALANCE = """
    UPDATE Users SET balance = balance + ? WHERE user_id = ?
    """;

    private static final String DELETE_CRYPTO = """
    DELETE FROM Cryptocurrency WHERE symbol = ?
    """;

    private static final String DELETE_USER = """
    DELETE FROM Users WHERE user_id = ?
    """;

    private static final String SELECT_EXISTING_BOTS = """
    SELECT bot_id, crypto_id, volatility
    FROM Bots
    """;

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

    @Override
    public Map<Long, Double> deleteCryptoWithBot(String symbol)
            throws DbConnectionException, CryptoNotFoundException {

        Map<Long, Double> refunds = new HashMap<>();

        try (Connection conn = DbConnectionSingleton.getInstance().connect()) {
            conn.setAutoCommit(false);

            try {
                long botUserId = fetchBotUserId(conn, symbol);
                double currentPrice = fetchCurrentPrice(conn, symbol);

                // Fetch all holders and compute refunds (skip the bot user)
                try (PreparedStatement ps = conn.prepareStatement(SELECT_HOLDERS)) {
                    ps.setString(1, symbol);
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

                // Delete crypto (cascades Bots, Portfolio, Crypto_History)
                try (PreparedStatement ps = conn.prepareStatement(DELETE_CRYPTO)) {
                    ps.setString(1, symbol);
                    if (ps.executeUpdate() == 0)
                        throw new CryptoNotFoundException(
                                "Crypto '" + symbol + "' not found.");
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

    @Override
    public List<Bot> loadInitialData(CryptoManager cryptoManager, boolean seedFromJson)
            throws CryptoNotAddedException, DbConnectionException, FileNotFoundException, BotGenerationException {

        Gson gson = new Gson();

        try (Connection conn = DbConnectionSingleton.getInstance().connect();
             FileReader fileReader = new FileReader(CRYPTO_FILEPATH)) {
            Crypto[] cryptos = gson.fromJson(fileReader, Crypto[].class);

            if (cryptos != null) {
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
}
