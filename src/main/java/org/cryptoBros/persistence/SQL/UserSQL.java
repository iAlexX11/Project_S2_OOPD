package org.cryptoBros.persistence.SQL;

import org.cryptoBros.business.User;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
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
    public User addUser(User user) throws UserNotAddException, DbConnectionException {
        String query = "INSERT INTO users (username, email, password, balance) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = db.connect().prepareStatement(
                query,
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setDouble(4, user.getBalance());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new UserNotAddException("Error inserting user into database");
            }

            var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                user.setId(generatedId); // update existing object
            }

            return user;

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

    @Override
    public void removeUser(int id) throws UserNotFoundException, DbConnectionException {
        String query = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement ps = db.connect().prepareStatement(query)) {

            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0)
            {
                throw new UserNotFoundException("Error deleting user: " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

    @Override
    public User getUser(String username, String email) throws UserNotFoundException, DbConnectionException {
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
                throw new UserNotFoundException("User with username " + username + " and email" + email + " not found");
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }
}
