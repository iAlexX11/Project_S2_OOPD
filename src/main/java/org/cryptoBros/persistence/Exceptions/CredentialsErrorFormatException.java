package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when user credentials fail format validation.
 */
public class CredentialsErrorFormatException extends RuntimeException {
	/**
	 * Creates a new CredentialsErrorFormatException with the given detail message.
	 *
	 * @param message the detail message
	 */
	public CredentialsErrorFormatException(String message) {
		super(message);
	}
}
