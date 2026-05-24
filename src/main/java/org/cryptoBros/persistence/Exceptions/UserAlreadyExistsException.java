package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when a user with the same username or email already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {
	/**
	 * Creates a new UserAlreadyExistsException with the given detail message.
	 *
	 * @param message the detail message
	 */
	public UserAlreadyExistsException(String message) {
		super(message);
	}
}
