package org.cryptoBros.business;
import org.cryptoBros.persistence.Exceptions.*;
import org.cryptoBros.persistence.SQL.UserSQL;
import org.cryptoBros.persistence.UserPersistence;
import org.passay.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.passay.EnglishCharacterData.*;

public class AccountManager {

	private final UserManager userManager;
    private final UserPersistence userPersistence;
	private final CredentialManager credentialManager;

	public AccountManager() {
		this.userManager = new  UserManager();
        userPersistence = new UserSQL();
		this.credentialManager = new CredentialManager();
	}

	public int signUpLogic(String email, char[] password, char[] confirmPassword, String username)
			throws DbConnectionException, UserNotAddException, UserAlreadyExistsException, CredentialsErrorFormatException  {

		String errorCredentials = credentialManager.checkCredentials(email, password, confirmPassword);
		if (!errorCredentials.equals("ok")) {
			throw new CredentialsErrorFormatException(errorCredentials);
		}

		String hashedPassword = credentialManager.hashPassword(password);

		try {
			userManager.getUser(username, email);
			throw new UserAlreadyExistsException("Username or email is already taken.");

		} catch (UserNotFoundException e) {
			User user = new User(username, email, hashedPassword);
			User userWithId = userManager.addUser(user);
            return userWithId.getId();
		} catch (DbConnectionException ex) {
			throw ex;
		}
	}

	public int logInNormalUser(String usernameOrEmail, char[] password) throws UserNotFoundException, CredentialsErrorFormatException, DbConnectionException {
		try {
			User user = userManager.getUser(usernameOrEmail, usernameOrEmail);
			if (credentialManager.checkHashedPassword(password, user.getPassword())) {
				return user.getId();
			} else {
				throw new CredentialsErrorFormatException("The username/email or password is incorrect.");
			}
		} catch (UserNotFoundException | DbConnectionException e) {
			throw e;
		}
	}

    public void deleteUser(int id) throws UserNotFoundException, DbConnectionException {
        userPersistence.removeUser(id);
    }
}