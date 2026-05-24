package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when a user could not be added to the database.
 */
public class UserNotAddException extends Exception {
    /**
     * Creates a new UserNotAddException with the given detail message.
     *
     * @param message the detail message
     */
    public UserNotAddException(String message) {
        super(message);
    }
}
