package org.cryptoBros.persistence.SQL;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.business.User;
import org.cryptoBros.persistence.CryptoPersistence;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class CryptoSQL implements CryptoPersistence {
    private DbConnectionSingleton db;

    public CryptoSQL() {
        db = DbConnectionSingleton.getInstance();
    }

    @Override
    public Crypto getCrypto(String name) {
        String query = "SELECT * FROM cryptocurrency WHERE name = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {
            ps.setString(1, name);

            var rs = ps.executeQuery();
            return new Crypto(
                    rs.getString("name"),
                    rs.getDouble("current_price"),
                    rs.getDouble("original_price")
            );

        } catch (Exception e) {
            System.err.println("Error fetching user: " + e.getMessage());
        }
        db.disconnect();
        return null;
    }

    @Override
    public List<Crypto> getAllCrypto() {
        String query = "SELECT * FROM cryptocurrency";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {
            var rs = ps.executeQuery();

            List<Crypto> cryptos = new ArrayList<>();
            while (rs.next()) {
                cryptos.add(new Crypto(
                        rs.getString("name"),
                        rs.getDouble("current_price"),
                        rs.getDouble("original_price")
                ));
            }
            return cryptos;
        } catch (Exception e) {
            System.err.println("Error fetching user: " + e.getMessage());
        }
        db.disconnect();
        return null;
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
            System.err.println("Error inserting user: " + e.getMessage());
        }
        db.disconnect();
    }

    @Override
    public void removeCrypto(String name) {
        String query = "DELETE FROM cryptocurrency WHERE name = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        db.disconnect();
    }

    @Override
    public void updateCrypto(String name, Crypto newCrypto) {

    }

    @Override
    public void updatePrice(String name, double newPrice) {

    }
}
