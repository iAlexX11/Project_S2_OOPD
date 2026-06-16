package org.cryptoBros.persistence.SQL;

import org.cryptoBros.business.User;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.persistence.UserPersistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQL implementation of UserPersistence using PostgreSQL.
 */
public class UserSQL implements UserPersistence {
    private final DbConnectionSingleton db;

    /**
     * Creates a new UserSQL instance.
     */
    public UserSQL() {
        this.db = DbConnectionSingleton.getInstance();
    }

    /**
     * Inserts a new user into the database and sets the generated ID on the returned object.
     *
     * @param user the user to add (ID field is ignored; a new ID is generated)
     * @return the same user object with its generated database ID set
     * @throws UserNotAddException   if the insert affected zero rows
     * @throws DbConnectionException if the database connection fails
     */
    @Override
    public User addUser(User user) throws UserNotAddException, DbConnectionException {
        String query = "INSERT INTO users (username, email, password, balance) VALUES (?, ?, ?, ?)";

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(
                     query, PreparedStatement.RETURN_GENERATED_KEYS))
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

    /**
     * Deletes a user from the database by their user ID.
     *
     * @param id the ID of the user to remove
     * @throws UserNotFoundException if no user exists with the given ID
     * @throws DbConnectionException if the database connection fails
     */
    @Override
    public void removeUser(int id) throws UserNotFoundException, DbConnectionException {
        String query = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

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

    /**
     * Retrieves a user from the database matching the given username or email.
     *
     * @param username the username to search for
     * @param email    the email to search for
     * @return the matching User object
     * @throws UserNotFoundException if no user matches the given username or email
     * @throws DbConnectionException if the database connection fails
     */
    @Override
    public User getUser(String username, String email) throws UserNotFoundException, DbConnectionException {
        String query = "SELECT * FROM users WHERE username = ? OR email = ?";

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

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
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

	/**
	 * Retrieves the current balance for a user by their user ID.
	 *
	 * @param id the ID of the user whose balance to retrieve
	 * @return the user's current balance
	 * @throws UserNotFoundException if no user exists with the given ID
	 * @throws DbConnectionException if the database connection fails
	 */
	@Override
	public double getUserBalance(long id) throws UserNotFoundException, DbConnectionException {
		String query = "SELECT balance FROM users WHERE user_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setLong(1, id);

			var rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("balance");
			}
            else {
                throw new UserNotFoundException("User with user_id " + id + " not found");
            }

		} catch (SQLException e) {
			throw new DbConnectionException("Error fetching user: " + e.getMessage());
		}
	}

	/**
	 * Overwrites the balance for a user with the specified new value.
	 *
	 * @param userId     the ID of the user whose balance to update
	 * @param newBalance the new balance to set
	 * @throws UserNotFoundException if no user exists with the given ID
	 * @throws DbConnectionException if the database connection fails
	 */
	@Override
	public void updateUserBalance(long userId, double newBalance) throws UserNotFoundException, DbConnectionException {
		String query = "UPDATE users SET balance = ? WHERE user_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setDouble(1, newBalance);
			ps.setLong(2, userId);

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0) {
				throw new UserNotFoundException("User with user_id " + userId + " not found");
			}
		} catch (SQLException e) {
			throw new DbConnectionException("Error updating balance: " + e.getMessage());
		}
	}

	/**
	 * Adjusts a user's balance by adding the given amount and returns the resulting balance.
	 * Uses UPDATE ... RETURNING to atomically apply the delta and read the new value.
	 *
	 * @param userId the ID of the user whose balance to adjust
	 * @param amount the amount to add (positive) or subtract (negative)
	 * @return the new balance after the adjustment
	 * @throws UserNotFoundException if no user exists with the given ID
	 * @throws DbConnectionException if the database connection fails
	 */
	@Override
	public double adjustUserBalance(long userId, double amount) throws UserNotFoundException, DbConnectionException {
		String query = "UPDATE users SET balance = balance + ? WHERE user_id = ? RETURNING balance";

		try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setDouble(1, amount);
			ps.setLong(2, userId);

			var rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getDouble("balance");
			} else {
				throw new UserNotFoundException("User with user_id " + userId + " not found");
			}
		} catch (SQLException e) {
			throw new DbConnectionException("Error adjusting balance: " + e.getMessage());
		}
	}

	/**
	 * Updates the username for the given user in the database.
	 *
	 * @param username the new username to set
	 * @param userId   the ID of the user whose username to change
	 * @throws DbConnectionException if the database connection fails
	 */
	@Override
	public void changeUsername(String username, int userId) throws DbConnectionException {
		String query = "UPDATE users SET username = ? WHERE user_id = ?";

		try (PreparedStatement ps = db.connect().prepareStatement(query)) {
			ps.setString(1, username);
			ps.setInt(2, userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DbConnectionException("Something happened, please try again later!");
		}
	}

	/**
	 * Updates the password for the given user in the database.
	 *
	 * @param password the new password to set
	 * @param userId   the ID of the user whose password to change
	 * @throws DbConnectionException if the database connection fails
	 */
	@Override
	public void changePassword(String password, int userId) throws DbConnectionException {
		String query = "UPDATE users SET password = ? WHERE user_id = ?";

		try (PreparedStatement ps = db.connect().prepareStatement(query)) {
			ps.setString(1, password);
			ps.setInt(2, userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DbConnectionException("Something happened, please try again later!");
		}
	}
}
