package org.cryptoBros.business;

import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.persistence.SQL.UserSQL;
import org.cryptoBros.persistence.UserPersistence;

public class UserManager {
	private User currentUser;
	private final UserPersistence userPersistence = new UserSQL();

	public User addUser(User user) throws UserNotAddException {
		return userPersistence.addUser(user);
	}

	public User getUser(String username, String email) throws UserNotFoundException {
		return userPersistence.getUser(username, email);
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}
}
