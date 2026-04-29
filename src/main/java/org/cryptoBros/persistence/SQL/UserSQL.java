package org.cryptoBros.persistence.SQL;

import org.cryptoBros.business.User;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.persistence.UserPersistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserSQL implements UserPersistence {
    private final DbConnectionSingleton db;

    public UserSQL() {
        this.db = DbConnectionSingleton.getInstance();
    }

    @Override
    public User addUser(User user) throws UserNotAddException {
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

        } catch (SQLException e) {
            throw new UserNotAddException("Error inserting user: " + e.getMessage());
        }
    }

    @Override
    public void removeUser(int id) throws UserNotFoundException {
        String query = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new UserNotFoundException("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public User getUser(String username, String email) throws UserNotFoundException {
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
            else {
                throw new UserNotFoundException("User not found");
            }
        } catch (SQLException e) {
            throw new UserNotFoundException("Error fetching user: " + e.getMessage());
        }
    }
}
