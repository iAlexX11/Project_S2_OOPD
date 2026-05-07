package org.cryptoBros.persistence.SQL;

import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.PurchaseNotAddedException;
import org.cryptoBros.persistence.Exceptions.SaleNotAddedException;
import org.cryptoBros.persistence.UserPortoflioPersistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPortfolioSQL implements UserPortoflioPersistence {

    @Override
    public void buyCrypto(int userId, String cryptoSymbol, double currentPrice, double units)
            throws PurchaseNotAddedException, DbConnectionException {
        // ON CONFLICT targets the composite PK (user_id, crypto_id).
        // On a duplicate key we just accumulate units; buy_price is kept from
        // the original purchase (trigger will update it by 1% regardless).
        String query = """
                INSERT INTO Portfolio (user_id, crypto_id, units, buy_price)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (user_id, crypto_id)
                DO UPDATE SET units = Portfolio.units + EXCLUDED.units
                """;

        try (PreparedStatement ps = DbConnectionSingleton.getInstance()
                .connect()
                .prepareStatement(query)) {

            ps.setInt(1, userId);
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

    @Override
    public void sellCrypto(int userId, String cryptoSymbol, double units)
            throws CryptoNotFoundException, SaleNotAddedException, DbConnectionException {

        // Fetch current units first so we can decide whether to UPDATE or DELETE.
        String selectQuery = "SELECT units FROM Portfolio WHERE user_id = ? AND crypto_id = ?";

        String updateQuery = "UPDATE Portfolio SET units = units - ? WHERE user_id = ? AND crypto_id = ?";

        String deleteQuery = "DELETE FROM Portfolio WHERE user_id = ? AND crypto_id = ?";

        try {
            var conn = DbConnectionSingleton.getInstance().connect();

            // Read how many units the user currently holds.
            double currentUnits;
            try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
                ps.setInt(1, userId);
                ps.setString(2, cryptoSymbol);

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    throw new CryptoNotFoundException(
                            "No position found for crypto: " + cryptoSymbol);
                }

                currentUnits = rs.getDouble("units");
            }

            if (units > currentUnits) {
                throw new SaleNotAddedException(
                        "Cannot sell more units than owned. Owned: " + currentUnits
                                + ", attempted: " + units);
            }

            if (Double.compare(units, currentUnits) == 0) {
                // Selling the entire position → delete the row.
                // the sell trigger fires on UPDATE only, so we UPDATE first
                // (triggering the 1% price drop) and then DELETE the zeroed row.
                try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
                    ps.setDouble(1, units);
                    ps.setInt(2, userId);
                    ps.setString(3, cryptoSymbol);
                    ps.executeUpdate(); // trigger fires here
                }
                try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
                    ps.setInt(1, userId);
                    ps.setString(2, cryptoSymbol);
                    ps.executeUpdate();
                }
            } else {
                // Partial sell → just update units (trigger fires automatically).
                try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
                    ps.setDouble(1, units);
                    ps.setInt(2, userId);
                    ps.setString(3, cryptoSymbol);

                    int affectedRows = ps.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SaleNotAddedException("Unexpected error processing the sale");
                    }
                }
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }
}