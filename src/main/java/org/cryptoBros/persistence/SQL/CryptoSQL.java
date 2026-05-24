package org.cryptoBros.persistence.SQL;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL implementation of {@link CryptoPersistence} using PostgreSQL.
 * Provides CRUD operations and price history queries for cryptocurrency data.
 */
public class CryptoSQL implements CryptoPersistence {
    private final DbConnectionSingleton db;

    /** Creates a new CryptoSQL instance. */
    public CryptoSQL() {
        db = DbConnectionSingleton.getInstance();
    }

    /**
     * Retrieves a cryptocurrency by its ticker symbol from the database.
     *
     * @param symbol the ticker symbol to search for
     * @return the matching Crypto object
     * @throws CryptoNotFoundException if no crypto matches the symbol
     * @throws DbConnectionException   if the database connection fails
     */
    @Override
    public Crypto getCrypto(String symbol) throws CryptoNotFoundException, DbConnectionException {
        String query = "SELECT * FROM cryptocurrency WHERE symbol = ?";

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Crypto(
                        rs.getString("symbol"),
                        rs.getString("name"),
                        rs.getDouble("current_price"),
                        rs.getDouble("original_price"),
                        rs.getDouble("volatility")
                );
            }
            else {
                throw new CryptoNotFoundException("Crypto with symbol " + symbol + " not found.");
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

    /**
     * Retrieves all cryptocurrencies from the database.
     *
     * @return a list of all Crypto objects
     * @throws CryptoNotFoundException if no cryptocurrencies exist in the database
     * @throws DbConnectionException   if the database connection fails
     */
    @Override
    public List<Crypto> getAllCrypto() throws CryptoNotFoundException, DbConnectionException {
        String query = "SELECT * FROM cryptocurrency";

        List<Crypto> cryptos = new ArrayList<>();

        try (PreparedStatement ps = db.connect().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cryptos.add(new Crypto(
                        rs.getString("symbol"),
                        rs.getString("name"),
                        rs.getDouble("current_price"),
                        rs.getDouble("original_price"),
                        rs.getDouble("volatility")
                ));
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }

        if (cryptos.isEmpty()) {
            throw new CryptoNotFoundException("No cryptocurrencies found in the database.");
        }

        return cryptos;
    }


    /**
     * Retrieves the price history for a cryptocurrency from the last 10 minutes, ordered ascending by timestamp.
     *
     * @param symbol the ticker symbol of the cryptocurrency
     * @return a linked map of timestamps to prices in chronological order
     * @throws CryptoNotFoundException if no price history exists for the symbol
     * @throws DbConnectionException   if the database connection fails
     */
    @Override
    public Map<Instant, Double> getPriceHistory(String symbol) throws CryptoNotFoundException, DbConnectionException {
        String query = """
                SELECT time_stamp, price
                FROM crypto_history ch
                WHERE ch.crypto_id = ?
                  AND ch.time_stamp >= CURRENT_TIMESTAMP - INTERVAL '10 minutes'
                ORDER BY ch.time_stamp ASC
            """;

        Map<Instant, Double> priceHistory = new LinkedHashMap<>();
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                priceHistory.put(rs.getTimestamp("time_stamp").toInstant(), rs.getDouble("price"));
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }

        if (priceHistory.isEmpty()) {
            throw new CryptoNotFoundException("No price history found for crypto with name '" + symbol + "'.");
        }

        return priceHistory;
    }

	/**
	 * Renames a cryptocurrency by updating its name column in the database.
	 *
	 * @param oldName the current name of the cryptocurrency
	 * @param newName the new name to assign
	 * @throws CryptoNotFoundException if no crypto matches the old name
	 * @throws DbConnectionException   if the database connection fails
	 */
	@Override
	public void changeCryptoName(String oldName, String newName) throws CryptoNotFoundException, DbConnectionException{
		String query = "UPDATE cryptocurrency SET name = ? WHERE name = ?";

		try (Connection conn = db.connect();
			 PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, newName);
			ps.setString(2, oldName);

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0)
			{
				throw new CryptoNotFoundException("Crypto with name '" + oldName + "' not found.");
			}

		} catch (SQLException e) {
			throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
		}
	}

}