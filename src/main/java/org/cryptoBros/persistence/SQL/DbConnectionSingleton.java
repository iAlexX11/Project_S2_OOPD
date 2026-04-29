package org.cryptoBros.persistence.SQL;

import org.cryptoBros.persistence.Config;
import org.cryptoBros.persistence.ConfigJson;
import org.cryptoBros.persistence.ConfigPersistence;
import org.cryptoBros.persistence.DbCredentials;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;

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

    // The static attribute to implement the singleton design pattern.
    private static DbConnectionSingleton instance = null;

    private final ConfigPersistence configPersistence;


    /**
     * Static method that returns the shared instance managed by the singleton.
     *
     * @return The shared SQLConnector instance.
     */
    public static DbConnectionSingleton getInstance() {
        if (instance == null ){
            instance = new DbConnectionSingleton();
        }
        return instance;
    }

    // Attributes to connect to the database.
    private String username;
    private String password;
    private String url;
    private Connection conn;

    // Parametrized constructor
    private DbConnectionSingleton() {
        configPersistence = new ConfigJson();
    }


    /**
     * Method that starts the inner connection to the database. Ideally, users would disconnect after
     * using the shared instance.
     */
    public Connection connect() {
        try {
            conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch(SQLException e) {
            throw new IllegalStateException("Couldn't connect to --> " + url + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Method that closes the inner connection to the database. Ideally, users would disconnect after
     * using the shared instance.
     */
    public void disconnect() throws SQLException {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new IllegalStateException("Problem when closing the connection --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
    }

    /**
     * This method needs to be called when initializing the singleton
     * for the first time
     */

    public void loadConfig() throws ConfigFileNotFoundException {
        DbCredentials dbCredentials = configPersistence.readCredentials();
        this.username = dbCredentials.username();
        this.password = dbCredentials.password();
        this.url = "jdbc:postgresql://" + dbCredentials.ip() + ":" + dbCredentials.port() + "/" + dbCredentials.dbName();
    }
}
