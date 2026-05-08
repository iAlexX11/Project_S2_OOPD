package org.cryptoBros.persistence.Exceptions;

public class ConfigFileCorruptedException extends Exception {
    public ConfigFileCorruptedException(String message) {
        super(message);
    }
}
