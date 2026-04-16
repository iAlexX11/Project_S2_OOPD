package org.cryptoBros.persistence.SQL;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.persistence.CryptoPersistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * This class
 */

public class CryptoSQL implements CryptoPersistence {
    private final DbConnectionSingleton db;

    public CryptoSQL() {
        db = DbConnectionSingleton.getInstance();
    }

    @Override
    public Crypto getCrypto(String name) {
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

        } catch (Exception e) {
            System.err.println("Error fetching crypto: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Crypto> getAllCrypto() {
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

        } catch (Exception e) {
            System.err.println("Error fetching cryptos: " + e.getMessage());
        }

        return cryptos;
    }

    @Override
    public void addCrypto(Crypto newCrypto) {
        String query = "INSERT INTO cryptocurrency (name, current_price, original_price) VALUES (?, ?, ?)";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setString(1, newCrypto.getName());
            ps.setDouble(2, newCrypto.getCurrentPrice());
            ps.setDouble(3, newCrypto.getInitialPrice());

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error inserting crypto: " + e.getMessage());
        }
    }

    @Override
    public void removeCrypto(String name) {
        String query = "DELETE FROM cryptocurrency WHERE name = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setString(1, name);
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error deleting crypto: " + e.getMessage());
        }
    }

    @Override
    public void updateCrypto(String name, Crypto newCrypto) {
        String query = "UPDATE cryptocurrency SET current_price = ?, original_price = ? WHERE name = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setDouble(1, newCrypto.getCurrentPrice());
            ps.setDouble(2, newCrypto.getInitialPrice());
            ps.setString(3, name);

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error updating crypto: " + e.getMessage());
        }
    }

    @Override
    public void updatePrice(String name, double newPrice) {
        String query = "UPDATE cryptocurrency SET current_price = ? WHERE name = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setDouble(1, newPrice);
            ps.setString(2, name);

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error updating price: " + e.getMessage());
        }
    }
}