package org.cryptoBros.persistence.SQL;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.persistence.AtomicPersistence;
import org.cryptoBros.persistence.Exceptions.*;

import java.sql.*;

public class AtomicSQL implements AtomicPersistence {

    private static final String INSERT_USER = """
        INSERT INTO Users (username, email, password)
        VALUES (?, ?, ?)
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

    private static final String DELETE_CRYPTO = """
    DELETE FROM Cryptocurrency WHERE symbol = ?
    """;

    private static final String DELETE_USER = """
    DELETE FROM Users WHERE user_id = ?
    """;

    /**
     * Inserts a Users row, a Cryptocurrency row, and a Bots row
     * atomically in one connection/transaction.
     *
     * Correct FK order:
     *   Users  → must exist before Bots (bot_id FK)
     *   Cryptocurrency → must exist before Bots (crypto_id FK)
     *   Bots   → inserted last, both FKs satisfied
     *
     * @return the generated bot_id (= user_id)
     */
    @Override
    public int createCryptoWithBot(Crypto crypto)
            throws DbConnectionException, BotGenerationException, CryptoNotAddedException {

        try (Connection conn = DbConnectionSingleton.getInstance().connect()) {
            conn.setAutoCommit(false);

            try {
                int botUserId = insertUser(conn, crypto.getSymbol());
                insertCrypto(conn, crypto);
                insertBot(conn, botUserId, crypto.getSymbol(), crypto.getVolatility());

                conn.commit();
                return botUserId;

            } catch (BotGenerationException | CryptoNotAddedException e) {
                conn.rollback();   // nothing persisted if any step fails
                throw e;
            }

        } catch (SQLException e) {
            throw new DbConnectionException("DB connection error: " + e.getMessage());
        }
    }

    private int insertUser(Connection conn, String symbol)
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
                return keys.getInt(1);
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

    private void insertBot(Connection conn, int botUserId, String symbol, double volatility)
            throws SQLException, BotGenerationException {

        try (PreparedStatement ps = conn.prepareStatement(INSERT_BOT)) {
            ps.setInt   (1, botUserId);
            ps.setString(2, symbol);
            ps.setDouble(3, volatility);

            if (ps.executeUpdate() == 0)
                throw new BotGenerationException("Failed to insert bot for " + symbol);
        }
    }

    @Override
    public void deleteCryptoWithBot(String symbol)
            throws DbConnectionException, CryptoNotFoundException {

        try (Connection conn = DbConnectionSingleton.getInstance().connect()) {
            conn.setAutoCommit(false);

            try {
                // Resolve the bot's user_id before we lose the Bots row
                int botUserId = fetchBotUserId(conn, symbol);

                // Delete crypto cascades Bots, Portfolio, Crypto_History
                try (PreparedStatement ps = conn.prepareStatement(DELETE_CRYPTO)) {
                    ps.setString(1, symbol);
                    if (ps.executeUpdate() == 0)
                        throw new CryptoNotFoundException(
                                "Crypto '" + symbol + "' not found.");
                }

                // Delete the bot user (Bots row already gone via cascade)
                try (PreparedStatement ps = conn.prepareStatement(DELETE_USER)) {
                    ps.setInt(1, botUserId);
                    ps.executeUpdate();
                }

                conn.commit();

            } catch (CryptoNotFoundException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new DbConnectionException("DB connection error: " + e.getMessage());
        }
    }

    private int fetchBotUserId(Connection conn, String symbol)
            throws SQLException, CryptoNotFoundException {

        try (PreparedStatement ps = conn.prepareStatement(SELECT_BOT_ID)) {
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                throw new CryptoNotFoundException(
                        "No bot found for crypto '" + symbol + "'.");
            return rs.getInt("bot_id");
        }
    }

}