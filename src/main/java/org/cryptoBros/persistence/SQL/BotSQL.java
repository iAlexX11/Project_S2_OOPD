package org.cryptoBros.persistence.SQL;

import org.cryptoBros.persistence.BotPersistence;
import org.cryptoBros.persistence.Exceptions.BotGenerationException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

import java.sql.*;

public class BotSQL implements BotPersistence {

    @Override
    public int createBotUser(String cryptoSymbol, double volatility) throws DbConnectionException, BotGenerationException {

        // Synthetic but unique and identifiable email
        String email    = "bot." + cryptoSymbol + "@system.cryptobros.internal";
        String username = "BOT_" + cryptoSymbol;
        String password = ""; // bots never log in; empty string passes NOT NULL

        String insertUser = """
            INSERT INTO Users (username, email, password)
            VALUES (?, ?, ?)
            """;

        String insertBot = """
            INSERT INTO Bots (bot_id, crypto_id, volatility)
            VALUES (?, ?, ?)
            """;

        try (Connection conn = DbConnectionSingleton.getInstance().connect()) {
            conn.setAutoCommit(false); // both rows succeed or neither does

            int botUserId;

            try (PreparedStatement ps = conn.prepareStatement(
                    insertUser, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, password);
                int rows = ps.executeUpdate();
                if (rows == 0) throw new BotGenerationException("Couldn't insert bot user into database");

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new BotGenerationException("No user_id generated for bot");
                    botUserId = keys.getInt(1);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertBot)) {
                ps.setInt   (1, botUserId);
                ps.setString(2, cryptoSymbol);
                ps.setDouble(3, volatility);
                int rows = ps.executeUpdate();
                if (rows == 0) throw new BotGenerationException("Couldn't insert bot into database");
            }

            conn.commit();
            return botUserId;

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e);
        }
    }
}