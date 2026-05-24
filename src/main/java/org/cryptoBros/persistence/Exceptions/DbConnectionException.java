package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when a database connection or transaction fails.
 */
public class DbConnectionException extends Exception {
    /**
     * Creates a new DbConnectionException with the given detail message.
     *
     * @param message the detail message
     */
    public DbConnectionException(String message) {
        super(message);
    }
}
