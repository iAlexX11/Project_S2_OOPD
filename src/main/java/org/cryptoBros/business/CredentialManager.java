package org.cryptoBros.business;

import org.cryptoBros.persistence.ConfigJson;
import org.cryptoBros.persistence.ConfigPersistence;
import org.cryptoBros.persistence.Exceptions.ConfigFileCorruptedException;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;
import org.cryptoBros.persistence.SQL.DbConnectionSingleton;

public class CredentialManager {
    private final ConfigPersistence configPersistence;

    public CredentialManager() {
        configPersistence = new ConfigJson();
    }

    public void loadConfigFile() throws ConfigFileNotFoundException, ConfigFileCorruptedException {
        DbConnectionSingleton.getInstance().loadConfig();
    }

    public String readAdminPassword() throws ConfigFileNotFoundException {
        return configPersistence.readAdminPassword();
    }
}
