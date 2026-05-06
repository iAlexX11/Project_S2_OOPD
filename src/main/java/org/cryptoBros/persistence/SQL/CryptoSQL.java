package org.cryptoBros.persistence.SQL;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.Exceptions.CryptoNotAddedException;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class
 */

public class CryptoSQL implements CryptoPersistence {
    private final DbConnectionSingleton db;

    public CryptoSQL() {
        db = DbConnectionSingleton.getInstance();
    }

    @Override
    public Crypto getCrypto(String name) throws CryptoNotFoundException, DbConnectionException {
        String query = "SELECT * FROM cryptocurrency WHERE name = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Crypto(
                        rs.getString("name"),
                        rs.getDouble("current_price"),
                        rs.getDouble("original_price")
                );
            }
            else {
                throw new CryptoNotFoundException("Crypto with name " + name + " not found.");
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
                        rs.getString("name"),
                        rs.getDouble("current_price"),
                        rs.getDouble("original_price")
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
        String query = "INSERT INTO cryptocurrency (name, current_price, original_price) VALUES (?, ?, ?)";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setString(1, newCrypto.getName());
            ps.setDouble(2, newCrypto.getCurrentPrice());
            ps.setDouble(3, newCrypto.getInitialPrice());

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
    public void removeCrypto(String name) throws CryptoNotFoundException, DbConnectionException {
        String query = "DELETE FROM cryptocurrency WHERE name = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setString(1, name);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new CryptoNotFoundException("Crypto with name '" + name + "' not found.");
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

    @Override
    public void updateCrypto(String name, Crypto newCrypto) throws CryptoNotFoundException, DbConnectionException {
        String query = "UPDATE cryptocurrency SET name = ?, current_price = ?, original_price = ? WHERE name = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setString(1, newCrypto.getName());
            ps.setDouble(2, newCrypto.getCurrentPrice());
            ps.setDouble(3, newCrypto.getInitialPrice());
            ps.setString(4, name);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0)
            {
                throw new CryptoNotFoundException("Crypto with name '" + name + "' not found.");
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

    @Override
    public void updatePrice(String name, double newPrice) throws CryptoNotFoundException, DbConnectionException {
        String query = "UPDATE cryptocurrency SET current_price = ? WHERE name = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setDouble(1, newPrice);
            ps.setString(2, name);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0)
            {
                throw new CryptoNotFoundException("Crypto with name '" + name + "' not found.");
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

    @Override
    public List<Map<Instant, Double>> getPriceHistory(String cryptoName) throws CryptoNotFoundException, DbConnectionException {
        String query = "SELECT timestamp, price FROM crypto_history WHERE crypto_name = ? ORDER BY timestamp ASC";

        List<Map<Instant, Double>> priceHistory = new ArrayList<>();

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setString(1, cryptoName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<Instant, Double> entry = new HashMap<>();
                entry.put(rs.getTimestamp("timestamp").toInstant(), rs.getDouble("price"));
                priceHistory.add(entry);
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }

        if (priceHistory.isEmpty()) {
            throw new CryptoNotFoundException("No price history found for crypto with name '" + cryptoName + "'.");
        }

        return priceHistory;
    }
}