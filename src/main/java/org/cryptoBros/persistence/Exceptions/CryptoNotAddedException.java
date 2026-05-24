package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when a cryptocurrency cannot be added to the database.
 */
public class CryptoNotAddedException extends Exception {
    /**
     * Creates a new CryptoNotAddedException with the given detail message.
     *
     * @param message the detail message
     */
    public CryptoNotAddedException(String message) {
        super(message);
    }
}
