package org.cryptoBros.business;

import org.cryptoBros.persistence.ConfigJson;
import org.cryptoBros.persistence.ConfigPersistence;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;

public class CredentialManager {
    private final ConfigPersistence configPersistence;

    public CredentialManager() {
        configPersistence = new ConfigJson();
    }

    public String readAdminPassword() throws ConfigFileNotFoundException {
        return configPersistence.readAdminPassword();
    }
}
