package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when a user cannot be found in the database.
 */
public class UserNotFoundException extends Exception {
    /**
     * Creates a new UserNotFoundException with no detail message.
     */
    public UserNotFoundException() {
        super();
    }

    /**
     * Creates a new UserNotFoundException with the given detail message.
     *
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
