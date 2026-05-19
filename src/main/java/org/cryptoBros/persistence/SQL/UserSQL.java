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
        String query = "DELETE FROM users WHERE user_id = ?";

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
                throw new UserNotFoundException("User not found");
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Error connecting to the database: " + e.getMessage());
        }
    }

	@Override
	public double getUserBalance(long id) throws UserNotFoundException, DbConnectionException {
		String query = "SELECT balance FROM users WHERE user_id = ?";

		try (PreparedStatement ps = db.connect().prepareStatement(query)) {
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

	@Override
	public void updateUserBalance(long userId, double newBalance) throws UserNotFoundException, DbConnectionException {
		String query = "UPDATE users SET balance = ? WHERE user_id = ?";

		try (PreparedStatement ps = db.connect().prepareStatement(query)) {
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

	@Override
	public double adjustUserBalance(long userId, double amount) throws UserNotFoundException, DbConnectionException {
		String query = "UPDATE users SET balance = balance + ? WHERE user_id = ? RETURNING balance";

		try (PreparedStatement ps = db.connect().prepareStatement(query)) {
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
