package org.cryptoBros.business;

import org.cryptoBros.persistence.UserPersistence;

public class UserManager {
	private User currentUser;
	private final UserPersistence userPersistence;

	public UserManager(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public User addUser(User user) {
		return userPersistence.addUser(user);
	}

	public User getUser(String username, String email) {
		return userPersistence.getUser(username, email);
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}
}
