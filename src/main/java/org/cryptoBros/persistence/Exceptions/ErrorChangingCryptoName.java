package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when an error occurs while changing a cryptocurrency name.
 */
public class ErrorChangingCryptoName extends RuntimeException {
	/**
	 * Creates a new ErrorChangingCryptoName with the given detail message.
	 *
	 * @param message the detail message
	 */
	public ErrorChangingCryptoName(String message) {
		super(message);
	}
}
