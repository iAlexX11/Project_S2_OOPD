package org.cryptoBros.persistence;

import org.cryptoBros.persistence.Exceptions.ConfigFileCorruptedException;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;

/**
 * Defines operations for reading application configuration.
 */
public interface ConfigPersistence {
    /**
     * Reads the admin password from configuration.
     * @return the admin password
     * @throws ConfigFileNotFoundException if the config file is not found
     */
    String readAdminPassword() throws ConfigFileNotFoundException;

    /**
     * Reads database connection credentials from configuration.
     * @return the database credentials
     * @throws ConfigFileNotFoundException if the config file is not found
     * @throws ConfigFileCorruptedException if the config file is missing required fields
     */
    DbCredentials readCredentials() throws ConfigFileNotFoundException, ConfigFileCorruptedException;
}
