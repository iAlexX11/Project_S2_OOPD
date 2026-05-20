package org.cryptoBros.persistence.SQL;

import com.google.gson.Gson;
import org.cryptoBros.business.Crypto;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.Exceptions.CryptoNotAddedException;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

/**
 * This class
 */

public class CryptoSQL implements CryptoPersistence {
    private final DbConnectionSingleton db;

    public CryptoSQL() {
        db = DbConnectionSingleton.getInstance();
    }

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

    @Override
    public void addCrypto(Crypto newCrypto) throws CryptoNotAddedException,DbConnectionException {
        String query = """
            INSERT INTO Cryptocurrency (symbol, name, current_price, original_price, volatility)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT (symbol) DO NOTHING
        """;

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newCrypto.getSymbol());
            ps.setString(2, newCrypto.getName());
            ps.setDouble(3, newCrypto.getCurrentPrice());
            ps.setDouble(4, newCrypto.getInitialPrice());
            ps.setDouble(5, newCrypto.getVolatility());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0)
            {
                throw new CryptoNotAddedException("Failed to add crypto with name '" + newCrypto.getName() + "'.");
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

    @Override
    public void removeCrypto(String symbol) throws CryptoNotFoundException, DbConnectionException {
        String query = "DELETE FROM cryptocurrency WHERE symbol = ?";

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, symbol);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new CryptoNotFoundException("Crypto with name '" + symbol + "' not found.");
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

    @Override
    public void updatePrice(String symbol, double newPrice) throws CryptoNotFoundException, DbConnectionException {
        String query = "UPDATE cryptocurrency SET current_price = ? WHERE symbol = ?";

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDouble(1, newPrice);
            ps.setString(2, symbol);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0)
            {
                throw new CryptoNotFoundException("Crypto with name '" + symbol + "' not found.");
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

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

	@Override
	public void changeCryptoName(String symbol, String name) throws CryptoNotFoundException, DbConnectionException{
		String query = "UPDATE cryptocurrency SET name = ? WHERE symbol = ?";

		try (Connection conn = db.connect();
			 PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, name);
			ps.setString(2, symbol);

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0)
			{
				throw new CryptoNotFoundException("Crypto with name '" + symbol + "' not found.");
			}

		} catch (SQLException e) {
			throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
		}
	}

}