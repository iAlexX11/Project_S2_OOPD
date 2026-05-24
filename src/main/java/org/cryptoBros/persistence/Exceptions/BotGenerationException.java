package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when a bot user cannot be created for a cryptocurrency.
 */
public class BotGenerationException extends Exception {
    /**
     * Creates a new BotGenerationException with the given detail message.
     *
     * @param message the detail message
     */
    public BotGenerationException(String message) {
        super(message);
    }
}
