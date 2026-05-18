package org.cryptoBros.persistence.SQL;

import org.cryptoBros.persistence.Config;
import org.cryptoBros.persistence.ConfigJson;
import org.cryptoBros.persistence.ConfigPersistence;
import org.cryptoBros.persistence.DbCredentials;
import org.cryptoBros.persistence.Exceptions.ConfigFileCorruptedException;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;

import java.sql.*;

public class DbConnectionSingleton {

    private static volatile DbConnectionSingleton instance = null;

    private final ConfigPersistence configPersistence;

    public static DbConnectionSingleton getInstance() {
        if (instance == null) {
            synchronized (DbConnectionSingleton.class) {
                if (instance == null) {
                    instance = new DbConnectionSingleton();
                }
            }
        }
        return instance;
    }

    private String username;
    private String password;
    private String url;

    private DbConnectionSingleton() {
        configPersistence = new ConfigJson();
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void disconnect(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    public void loadConfig() throws ConfigFileNotFoundException, ConfigFileCorruptedException {
        DbCredentials dbCredentials = configPersistence.readCredentials();
        this.username = dbCredentials.username();
        this.password = dbCredentials.password();
        this.url = "jdbc:postgresql://" + dbCredentials.ip() + ":" + dbCredentials.port() + "/" + dbCredentials.dbName();
    }
}
