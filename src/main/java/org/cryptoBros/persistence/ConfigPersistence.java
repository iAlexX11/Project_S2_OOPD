package org.cryptoBros.persistence;

import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;

public interface ConfigPersistence {
    String readAdminPassword() throws ConfigFileNotFoundException;
    DbCredentials readCredentials() throws ConfigFileNotFoundException;
}
