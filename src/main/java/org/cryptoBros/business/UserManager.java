package org.cryptoBros.business;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.persistence.AtomicPersistence;
import org.cryptoBros.persistence.Exceptions.*;
import org.cryptoBros.persistence.SQL.AtomicSQL;
import org.cryptoBros.persistence.SQL.UserSQL;
import org.cryptoBros.persistence.UserPersistence;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Manages user state, balance operations, and periodic balance increases.
 */
public class UserManager {
	private int currentUserId; // -1;
	private final UserPersistence userPersistence;
    private final AtomicPersistence atomicDb;

    private BalanceListener balanceListeners;
    private ScheduledExecutorService scheduler;

    private static final double PERIODIC_INCREASE_AMOUNT = 10.0;

    /**
     * Creates a new UserManager.
     */
    public UserManager() {
        this.userPersistence = new UserSQL();
        this.atomicDb = new AtomicSQL();
    }

	/** Adds a user to the database.
	 *  @param user the user to add
	 *  @return the persisted user with generated id
	 *  @throws UserNotAddException if the user could not be added
	 *  @throws DbConnectionException if the db connection fails */
	public User addUser(User user) throws UserNotAddException, DbConnectionException {
		return userPersistence.addUser(user);
	}

	/** Retrieves a user by username or email.
	 *  @param username the username to search
	 *  @param email the email to search
	 *  @return the matching user
	 *  @throws UserNotFoundException if no user is found
	 *  @throws DbConnectionException if the db connection fails */
	public User getUser(String username, String email) throws UserNotFoundException, DbConnectionException {
		return userPersistence.getUser(username, email);
	}

	/** Returns the balance for a user.
	 *  @param userId the user id
	 *  @return the user's balance
	 *  @throws UserNotFoundException if the user is not found
	 *  @throws DbConnectionException if the db connection fails */
	public double getUserBalance(long userId) throws UserNotFoundException, DbConnectionException {
		return userPersistence.getUserBalance(userId);
	}

	/**
	 * Sets the current logged-in user.
	 * @param currentUserId the id of the logged-in user
	 */
	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}

	/**
	 * Returns the current user id.
	 * @return the id of the logged-in user
	 */
	public int getCurrentUserId() {
		return currentUserId;
	}

    /** Clears the current user session. */
    public void clearCurrentUser() {
        this.currentUserId = -1;
    }

	/**
	 * Replaces the balance listener.
	 * @param listener the listener to set, ignored if null
	 */
	public void changeBalanceListener(BalanceListener listener) {
		if (listener != null) {
            this.balanceListeners = listener;
		}
	}

	/**
	 * Adds funds to the current user's balance.
	 *
	 * @param amount the amount to add
	 * @throws UserNotFoundException if no user is logged in
	 * @throws DbConnectionException if the db connection fails
	 */
	public void addBalance(double amount) throws UserNotFoundException, DbConnectionException {
		if (currentUserId == -1) {
			throw new UserNotFoundException();
		}
		double newBalance = userPersistence.adjustUserBalance(currentUserId, amount);
		notifyBalanceListeners(newBalance);
	}

	/** Deducts funds from the current user's balance.
	 *  @param amount the amount to deduct
	 *  @throws UserNotFoundException if no user is logged in
	 *  @throws DbConnectionException if the db connection fails
	 *  @throws InsufficientBalanceException if the balance is too low */
	public void deductBalance(double amount) throws UserNotFoundException, DbConnectionException, InsufficientBalanceException {
		if (currentUserId == -1) {
			throw new UserNotFoundException();
		}
		double currentBalance = userPersistence.getUserBalance(currentUserId);
		if (currentBalance < amount) {
			throw new InsufficientBalanceException();
		}
		double newBalance = userPersistence.adjustUserBalance(currentUserId, -amount);
		notifyBalanceListeners(newBalance);
		//return newBalance;
	}

	private void notifyBalanceListeners(double newBalance) {
        if (balanceListeners == null) return;
		SwingUtilities.invokeLater(() -> {
            balanceListeners.balanceChanged(newBalance);
		});
	}

	/** Stops the periodic balance increase scheduler. */
	public void stopBalanceScheduler() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdownNow();
			scheduler = null;
		}
	}

    /** Removes the current balance listener. */
    public void clearBalanceListener() {
        balanceListeners = null;
    }

	/** Changes the current user's username.
	 *  @param newUsername the new username
	 *  @throws DbConnectionException if the db connection fails
	 *  @throws UsernameAlreadyExists if the username is taken */
	public void changeUsername(String newUsername) throws DbConnectionException,  UsernameAlreadyExists {
		try {
			userPersistence.getUser(newUsername, newUsername);
			throw new UsernameAlreadyExists("This username already exists");
		} catch (UserNotFoundException e) {
			userPersistence.changeUsername(newUsername, currentUserId);
		}
	}

	/** Changes the current user's password.
	 *  @param password the new hashed password
	 *  @throws DbConnectionException if the db connection fails */
	public void changeUserPassword(String password) throws DbConnectionException{
		userPersistence.changePassword(password, currentUserId);
	}

    /** Retrieves and deletes pending notifications.
     *  @return the list of pending notification messages
     *  @throws DbConnectionException if the db connection fails */
    public List<String> displayCryptoNotification() throws DbConnectionException {
        return atomicDb.popNotifications(currentUserId);
    }
}
