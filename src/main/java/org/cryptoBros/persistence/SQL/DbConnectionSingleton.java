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

/**
 * The SQLConnector class will abstract the specifics of the connection to a MySQL database.
 *
 * This class follows the Singleton design pattern to facilitate outside access while maintaining
 * a single instance, as having multiple connectors to a database is generally discouraged.
 *
 * Be aware that this class presents a simplified approach. Configuration parameters SHOULD NOT be
 * hardcoded and the use of Statements COULD be replaced by PreparedStatements to avoid SQL Injection.
 */
public class DbConnectionSingleton {

    private static volatile DbConnectionSingleton instance = null;

    private final ConfigPersistence configPersistence;
    private DataSource dataSource;


    /**
     * Static method that returns the shared instance managed by the singleton.
     *
     * @return The shared SQLConnector instance.
     */
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


    /**
     * Method that starts the inner connection to the database. Ideally, users would disconnect after
     * using the shared instance.
     * @return The connection to the database.
     * @throws SQLException if there is a problem when connecting to the database
     */
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

    /**
     * This method needs to be called when initializing the singleton
     * for the first time
     * @throws ConfigFileNotFoundException if the configuration file is not found
     */

    public void loadConfig() throws ConfigFileNotFoundException, ConfigFileCorruptedException {
        DbCredentials dbCredentials = configPersistence.readCredentials();

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://" + dbCredentials.ip() + ":" + dbCredentials.port() + "/" + dbCredentials.dbName());
        ds.setUsername(dbCredentials.username());
        ds.setPassword(dbCredentials.password());
        ds.setMaximumPoolSize(20);
        ds.setMinimumIdle(5);
        ds.setConnectionTimeout(10000);

        this.dataSource = ds;
    }
}
