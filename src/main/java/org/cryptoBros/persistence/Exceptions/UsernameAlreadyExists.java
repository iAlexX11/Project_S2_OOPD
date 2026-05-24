package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when the desired username is already taken.
 */
public class UsernameAlreadyExists extends RuntimeException {
	/**
	 * Creates a new UsernameAlreadyExists with the given detail message.
	 *
	 * @param message the detail message
	 */
	public UsernameAlreadyExists(String message) {
		super(message);
	}
}
