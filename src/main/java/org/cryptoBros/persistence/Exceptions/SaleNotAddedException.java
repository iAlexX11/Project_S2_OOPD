package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when a crypto sale could not be completed.
 */
public class SaleNotAddedException extends Exception {
    /**
     * Creates a new SaleNotAddedException with the given detail message.
     *
     * @param message the detail message
     */
    public SaleNotAddedException(String message) {
        super(message);
    }
}
