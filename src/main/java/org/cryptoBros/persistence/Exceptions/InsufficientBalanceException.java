package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when a user's balance is too low for a transaction.
 */
public class InsufficientBalanceException extends Exception {
    /**
     * Creates a new InsufficientBalanceException with no detail message.
     */
    public InsufficientBalanceException() {
        super();
    }

}
