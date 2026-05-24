package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when the configuration file cannot be found.
 */
public class ConfigFileNotFoundException extends Exception {
    /**
     * Creates a new ConfigFileNotFoundException with the given detail message.
     *
     * @param message the detail message
     */
    public ConfigFileNotFoundException(String message) {
        super(message);
    }
}
