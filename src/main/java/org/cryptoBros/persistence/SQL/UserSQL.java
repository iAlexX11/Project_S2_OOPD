package org.cryptoBros.persistence.SQL;

import org.cryptoBros.business.User;
import org.cryptoBros.persistence.UserPersistence;

import java.sql.PreparedStatement;

public class UserSQL implements UserPersistence {
    private final DbConnectionSingleton db;

    public UserSQL() {
        this.db = DbConnectionSingleton.getInstance();
    }

    @Override
    public User addUser(User user) {
        String query = "INSERT INTO users (username, email, password, balance) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = db.connect().prepareStatement(
                query,
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setDouble(4, user.getBalance());

            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                user.setId(generatedId); // update existing object
            }

            return user;

        } catch (Exception e) {
            System.err.println("Error inserting user: " + e.getMessage());
        }
        // TODO: throw exception user not found
        return null;
    }

    @Override
    public void removeUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public User getUser(String username, String email) {
        String query = "SELECT * FROM users WHERE username = ? OR email = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, email);

            var rs = ps.executeQuery();
			if (rs.next()) {
				return new User(
						rs.getInt("user_id"),
						rs.getString("username"),
						rs.getString("email"),
						rs.getString("password"),
						rs.getDouble("balance")
				);
			}
        } catch (Exception e) {
            System.err.println("Error fetching user: " + e.getMessage());
        }
        // TODO: throw exception user not found
        return null;
    }
}
