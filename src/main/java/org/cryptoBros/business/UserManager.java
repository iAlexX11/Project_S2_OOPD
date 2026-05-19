package org.cryptoBros.business;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.InsufficientBalanceException;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.persistence.SQL.UserSQL;
import org.cryptoBros.persistence.UserPersistence;

import javax.swing.SwingUtilities;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserManager {
	private int currentUserId; // -1;
	private final UserPersistence userPersistence;

    private BalanceListener balanceListeners;
    private ScheduledExecutorService scheduler;

    private static final double PERIODIC_INCREASE_AMOUNT = 10.0;
	private static final long INTERVAL_SECONDS = 10;

    public UserManager() {
        this.userPersistence = new UserSQL();
    }

	public User addUser(User user) throws UserNotAddException, DbConnectionException {
		return userPersistence.addUser(user);
	}

	public User getUser(String username, String email) throws UserNotFoundException, DbConnectionException {
		return userPersistence.getUser(username, email);
	}

	public double getUserBalance() throws UserNotFoundException, DbConnectionException {
		return userPersistence.getUserBalance(currentUserId);
	}

	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}

	public int getCurrentUserId() {
		return currentUserId;
	}

    public void clearCurrentUser() {
        this.currentUserId = -1;
    }

	public void updateBalanceListener(BalanceListener balanceListener) {
		this.balanceListeners = balanceListener;
	}

	public void changeBalanceListener(BalanceListener listener) {
		if (listener != null) {
            this.balanceListeners = listener;
		}
	}

	public double addBalance(double amount) throws UserNotFoundException, DbConnectionException {
		if (currentUserId == -1) {
			throw new UserNotFoundException();
		}
		double newBalance = userPersistence.adjustUserBalance(currentUserId, amount);
		notifyBalanceListeners(newBalance);
		return newBalance;
	}

	public double deductBalance(double amount) throws UserNotFoundException, DbConnectionException, InsufficientBalanceException {
		if (currentUserId == -1) {
			throw new UserNotFoundException();
		}
		double currentBalance = userPersistence.getUserBalance(currentUserId);
		if (currentBalance < amount) {
			throw new InsufficientBalanceException();
		}
		double newBalance = userPersistence.adjustUserBalance(currentUserId, -amount);
		notifyBalanceListeners(newBalance);
		return newBalance;
	}

	private void notifyBalanceListeners(double newBalance) {
		SwingUtilities.invokeLater(() -> {
            balanceListeners.balanceChanged(newBalance);
		});
	}

	public void startBalanceScheduler() {
		if (scheduler != null && !scheduler.isShutdown()) {
			return;
		}
		scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "BalanceScheduler-Thread");
			t.setDaemon(true);
			return t;
		});
		scheduler.scheduleAtFixedRate(this::periodicBalanceIncrease, INTERVAL_SECONDS, INTERVAL_SECONDS, TimeUnit.SECONDS);
	}

	public void stopBalanceScheduler() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdownNow();
			scheduler = null;
		}
	}

	private void periodicBalanceIncrease() {
		if (currentUserId == -1) {
			return;
		}
		try {
			addBalance(PERIODIC_INCREASE_AMOUNT);
		} catch (UserNotFoundException | DbConnectionException e) {
			// Silent fail - scheduler continues
		}
	}

    public void clearBalanceListener() {
        balanceListeners = null;
    }

	public void changeUsername(String username) {
		userPersistence.changeUsername(username, currentUserId);
	}

	public void changeUserPassword(String password) {
		userPersistence.changePassword(password, currentUserId);
	}
}
