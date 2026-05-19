package org.cryptoBros.persistence.SQL;

import com.zaxxer.hikari.HikariDataSource;
import org.cryptoBros.persistence.Config;
import org.cryptoBros.persistence.ConfigJson;
import org.cryptoBros.persistence.ConfigPersistence;
import org.cryptoBros.persistence.DbCredentials;
import org.cryptoBros.persistence.Exceptions.ConfigFileCorruptedException;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;

import javax.sql.DataSource;
import java.sql.*;

public class DbConnectionSingleton {

    private static volatile DbConnectionSingleton instance = null;

    private final ConfigPersistence configPersistence;
    private DataSource dataSource;

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

    private DbConnectionSingleton() {
        configPersistence = new ConfigJson();
    }

    public Connection connect() throws SQLException {
        if (dataSource == null)
            throw new SQLException("DataSource not initialised — call loadConfig() first.");
        return dataSource.getConnection();
    }

    public void disconnect(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    public void loadConfig() throws ConfigFileNotFoundException, ConfigFileCorruptedException {
        DbCredentials dbCredentials = configPersistence.readCredentials();

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://" + dbCredentials.ip() + ":" + dbCredentials.port() + "/" + dbCredentials.dbName());
        ds.setUsername(dbCredentials.username());
        ds.setPassword(dbCredentials.password());
        ds.setMaximumPoolSize(10);
        ds.setMinimumIdle(2);
        ds.setConnectionTimeout(3000);

        this.dataSource = ds;
    }
}
