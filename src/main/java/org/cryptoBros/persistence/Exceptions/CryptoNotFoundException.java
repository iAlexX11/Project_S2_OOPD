package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when a cryptocurrency cannot be found in the database.
 */
public class CryptoNotFoundException extends Exception {
    /**
     * Creates a new CryptoNotFoundException with the given detail message.
     *
     * @param message the detail message
     */
    public CryptoNotFoundException(String message) {
        super(message);
    }
}
