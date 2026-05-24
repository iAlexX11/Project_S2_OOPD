package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when the configuration file contains invalid or missing data.
 */
public class ConfigFileCorruptedException extends Exception {
    /**
     * Creates a new ConfigFileCorruptedException with the given detail message.
     *
     * @param message the detail message
     */
    public ConfigFileCorruptedException(String message) {
        super(message);
    }
}
