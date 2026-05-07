package org.cryptoBros.business;

import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.persistence.SQL.UserSQL;
import org.cryptoBros.persistence.UserPersistence;

public class UserManager {
	private int currentUserId; // -1;
	private final UserPersistence userPersistence = new UserSQL();
	private BalanceListener balanceListener;

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
		this.balanceListener = balanceListener;
	}

    public void clearBalanceListener() {
        this.balanceListener = null;
    }

    public void clearCurrentUser() {
        this.currentUserId = -1;
    }
}
