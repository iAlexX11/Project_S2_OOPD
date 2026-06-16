package org.cryptoBros.persistence.SQL;

import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.PurchaseNotAddedException;
import org.cryptoBros.persistence.Exceptions.SaleNotAddedException;
import org.cryptoBros.persistence.PortfolioPosition;
import org.cryptoBros.persistence.UserPortfolioPersistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL implementation of UserPortfolioPersistence using PostgreSQL.
 */
public class UserPortfolioSQL implements UserPortfolioPersistence {
    private final DbConnectionSingleton db;

    /**
     * Creates a new UserPortfolioSQL instance.
     */
    public UserPortfolioSQL() {
        this.db = DbConnectionSingleton.getInstance();
    }

    /**
     * Records a crypto purchase for a user, inserting a new portfolio row or accumulating units on an existing one.
     * Uses ON CONFLICT to upsert; the database trigger raises the crypto price by 1% on insert/update.
     *
     * @param userId       the ID of the user buying the crypto
     * @param cryptoSymbol the ticker symbol of the cryptocurrency to buy
     * @param currentPrice the current price per unit at time of purchase
     * @param units        the number of units to buy
     * @throws PurchaseNotAddedException if units or price are not positive, or the insert affected zero rows
     * @throws DbConnectionException    if the database connection fails
     */
    @Override
    public void buyCrypto(long userId, String cryptoSymbol, double currentPrice, double units)
            throws PurchaseNotAddedException, DbConnectionException {

        if (units <= 0) {
            throw new PurchaseNotAddedException("Units cannot be negative or equal to zero");
        }

        if (currentPrice <= 0) {
            throw new PurchaseNotAddedException("Current price cannot be negative or equal to zero");
        }

        // ON CONFLICT targets the composite PK (user_id, crypto_id).
        // On a duplicate key we just accumulate units; buy_price remains
        // unchanged by this statement.
        String query = """
                INSERT INTO Portfolio (user_id, crypto_id, units, buy_price)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (user_id, crypto_id)
                DO UPDATE SET units = Portfolio.units + EXCLUDED.units
                """;

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, userId);
            ps.setString(2, cryptoSymbol);
            ps.setDouble(3, units);
            ps.setDouble(4, currentPrice);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new PurchaseNotAddedException("Unexpected error adding the purchase");
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

    /**
     * Sells crypto units for a user by decreasing the held amount or deleting the position entirely.
     * If all units are sold, the row is updated first (triggering the 1% price drop) and then deleted.
     * Partial sells only update the units column.
     *
     * @param userId       the ID of the user selling the crypto
     * @param cryptoSymbol the ticker symbol of the cryptocurrency to sell
     * @param units        the number of units to sell
     * @throws CryptoNotFoundException if the user has no position for the given crypto
     * @throws SaleNotAddedException   if the user attempts to sell more units than owned, or an unexpected error occurs
     * @throws DbConnectionException   if the database connection fails
     */
    @Override
    public void sellCrypto(long userId, String cryptoSymbol, double units)
            throws CryptoNotFoundException, SaleNotAddedException, DbConnectionException {

        String updateQuery = "UPDATE Portfolio SET units = units - ? WHERE user_id = ? AND crypto_id = ? AND  units >= ?";

        String deleteQuery = "DELETE FROM Portfolio WHERE user_id = ? AND crypto_id = ? AND  units = 0";

        String existsQuery = "SELECT 1 FROM Portfolio WHERE user_id = ? AND crypto_id = ?";


        try (Connection conn = DbConnectionSingleton.getInstance().connect();){
            conn.setAutoCommit(false);

            try {
                // Read how many units the user currently holds.
                try (PreparedStatement ps = conn.prepareStatement(existsQuery)) {
                    ps.setLong(1, userId);
                    ps.setString(2, cryptoSymbol);

                    if (!ps.executeQuery().next()) {
                        throw new CryptoNotFoundException(
                                "No position found for crypto: " + cryptoSymbol);
                    }
                }

                int affected;
                try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
                    ps.setDouble(1, units);
                    ps.setLong(2, userId);
                    ps.setString(3, cryptoSymbol);
                    ps.setDouble(4, units);
                    affected = ps.executeUpdate();
                }

                if (affected == 0) {
                    throw new SaleNotAddedException(
                            "Cannot sell more units than owned for: " + cryptoSymbol);
                }

                try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
                    ps.setLong(1, userId);
                    ps.setString(2, cryptoSymbol);
                    ps.executeUpdate();
                }

                conn.commit();
            } catch (CryptoNotFoundException | SaleNotAddedException e) {
                conn.rollback();
                throw e;
            } catch (SQLException e) {
                conn.rollback();
                throw new DbConnectionException("Error processing sale: " + e.getMessage());
        }
        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }

    }

    /**
     * Retrieves all portfolio positions for a user by joining Portfolio with Cryptocurrency for current prices.
     *
     * @param userId the ID of the user whose portfolio to retrieve
     * @return a list of portfolio positions including symbol, units, buy price, and current price; empty if none
     * @throws DbConnectionException if the database connection fails
     */
    @Override
    public List<PortfolioPosition> getUserPortfolio(long userId) throws DbConnectionException {
        String query = """
                SELECT p.crypto_id, p.units, p.buy_price, c.current_price
                FROM Portfolio p
                JOIN Cryptocurrency c ON p.crypto_id = c.symbol
                WHERE p.user_id = ?
                """;

        List<PortfolioPosition> positions = new ArrayList<>();

        try (Connection conn = DbConnectionSingleton.getInstance().connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                positions.add(new PortfolioPosition(
                        rs.getString("crypto_id"),
                        rs.getDouble("units"),
                        rs.getDouble("buy_price"),
                        rs.getDouble("current_price")
                ));
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }

        return positions;
    }
}