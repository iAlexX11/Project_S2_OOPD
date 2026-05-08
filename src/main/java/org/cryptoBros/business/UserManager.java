package org.cryptoBros.business;

import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.InsufficientBalanceException;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.persistence.SQL.UserSQL;
import org.cryptoBros.persistence.UserPersistence;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserManager {
	private int currentUserId = -1;
	private final UserPersistence userPersistence = new UserSQL();

	private final List<BalanceListener> balanceListeners = new CopyOnWriteArrayList<>();
	private ScheduledExecutorService scheduler;

	private static final double PERIODIC_INCREASE_AMOUNT = 10.0;
	private static final long INTERVAL_SECONDS = 60;

	public User addUser(User user) throws UserNotAddException, DbConnectionException {
		return userPersistence.addUser(user);
	}

	public User getUser(String username, String email) throws UserNotFoundException, DbConnectionException {
		return userPersistence.getUser(username, email);
	}

	public double getUserBalance(int id) throws UserNotFoundException, DbConnectionException {
		return userPersistence.getUserBalance(id);
	}

	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}

	public int getCurrentUserId() {
		return currentUserId;
	}

	public void updateBalanceListener(BalanceListener balanceListener) {
		addBalanceListener(balanceListener);
	}

    public void clearBalanceListener() {
        balanceListeners.clear();
    }

    public void clearCurrentUser() {
        this.currentUserId = -1;
    }

	public void addBalanceListener(BalanceListener listener) {
		if (listener != null && !balanceListeners.contains(listener)) {
			balanceListeners.add(listener);
		}
	}

	public void removeBalanceListener(BalanceListener listener) {
		balanceListeners.remove(listener);
	}

	public double addBalance(double amount) throws UserNotFoundException, DbConnectionException {
		if (currentUserId == -1) {
			throw new UserNotFoundException("No user is currently logged in");
		}
		double newBalance = userPersistence.adjustUserBalance(currentUserId, amount);
		notifyBalanceListeners(newBalance);
		return newBalance;
	}

	public double deductBalance(double amount) throws UserNotFoundException, DbConnectionException, InsufficientBalanceException {
		if (currentUserId == -1) {
			throw new UserNotFoundException("No user is currently logged in");
		}
		double currentBalance = userPersistence.getUserBalance(currentUserId);
		if (currentBalance < amount) {
			throw new InsufficientBalanceException("Insufficient balance. Current: " + currentBalance + ", Required: " + amount);
		}
		double newBalance = userPersistence.adjustUserBalance(currentUserId, -amount);
		notifyBalanceListeners(newBalance);
		return newBalance;
	}

	private void notifyBalanceListeners(double newBalance) {
		SwingUtilities.invokeLater(() -> {
			for (BalanceListener listener : balanceListeners) {
				listener.balanceChanged(newBalance);
			}
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
			scheduler.shutdown();
			try {
				if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
					scheduler.shutdownNow();
				}
			} catch (InterruptedException e) {
				scheduler.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}

	private void periodicBalanceIncrease() {
		if (currentUserId == -1) {
			return;
		}
		try {
			addBalance(PERIODIC_INCREASE_AMOUNT);
		} catch (UserNotFoundException | DbConnectionException e) {
			System.err.println("Failed to increase balance: " + e.getMessage());
		}
	}
}
