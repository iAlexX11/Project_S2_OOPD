package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when attempting to rename a crypto to an existing name.
 */
public class CryptoNameAlreadyExists extends RuntimeException {
	/**
	 * Creates a new CryptoNameAlreadyExists with the given detail message.
	 *
	 * @param message the detail message
	 */
	public CryptoNameAlreadyExists(String message) {
		super(message);
	}
}
