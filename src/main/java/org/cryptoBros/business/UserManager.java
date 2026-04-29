package org.cryptoBros.business;

import org.cryptoBros.persistence.SQL.UserSQL;
import org.cryptoBros.persistence.UserPersistence;

public class UserManager {
	private int currentUserId;
	private final UserPersistence userPersistence = new UserSQL();

	public User addUser(User user) {
		return userPersistence.addUser(user);
	}

	public User getUser(String username, String email) {
		return userPersistence.getUser(username, email);
	}

	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}

	public int getCurrentUserId() {
		return currentUserId;
	}
}
