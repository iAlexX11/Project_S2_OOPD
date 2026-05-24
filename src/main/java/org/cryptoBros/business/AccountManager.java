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

/**
 * Handles user registration and login operations
 */
public class AccountManager {

	private final UserManager userManager;
    private final UserPersistence userPersistence;
	private final CredentialManager credentialManager;

	/**
	 * Creates an AccountManager, initializing its dependencies
	 */
	public AccountManager() {
		this.userManager = new  UserManager();
        userPersistence = new UserSQL();
		this.credentialManager = new CredentialManager();
	}

	/**
	 * Validates credentials, hashes the password and creates a new user
	 * @param email the email of the new user
	 * @param password the password of the new user
	 * @param confirmPassword the password confirmation to verify it matches
	 * @param username the username of the new user
	 * @return the id of the newly created user
	 * @throws DbConnectionException if the db connection fails at any point during this process
	 * @throws UserNotAddException if the user could not be added to the db
	 * @throws UserAlreadyExistsException if a user with the same username or email already exists
	 * @throws CredentialsErrorFormatException if the credentials do not meet the required format
	 */
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

	/**
	 * Authenticates a user by username or email and password
	 * @param usernameOrEmail the username or email of the user
	 * @param password the password of the user
	 * @return the id of the authenticated user
	 * @throws UserNotFoundException if no user with the given username or email exists
	 * @throws CredentialsErrorFormatException if the username/email or password is incorrect
	 * @throws DbConnectionException if the db connection fails at any point during this process
	 */
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

    /**
     * Removes a user from the system
     * @param id the id of the user to be removed
     * @throws UserNotFoundException if no user with the given id exists
     * @throws DbConnectionException if the db connection fails at any point during this process
     */
    public void deleteUser(int id) throws UserNotFoundException, DbConnectionException {
        userPersistence.removeUser(id);
    }
}