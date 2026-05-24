package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when a crypto purchase could not be completed.
 */
public class PurchaseNotAddedException extends Exception {
    /**
     * Creates a new PurchaseNotAddedException with the given detail message.
     *
     * @param message the detail message
     */
    public PurchaseNotAddedException(String message) {
        super(message);
    }
}
