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
    public static DbConnectionSingleton getInstance(){
        if (instance == null ){
            // NOT a good practice to hardcode connection data! Be aware of this for your project delivery ;)
            instance = new DbConnectionSingleton();
            // do something like  configPersistence.readCredentials()  to get the credentials;
            instance.connect();
        }
        return instance;
    }

    // Attributes to connect to the database.
    private final String username;
    private final String password;
    private final String url;
    private Connection conn;

    // Parametrized constructor
    private DbConnectionSingleton() {
        String username;
        String password;
        String url;

        configPersistence = new ConfigJson();

        try {
            DbCredentials dbCredentials = configPersistence.readCredentials();
            username = dbCredentials.username();
            password = dbCredentials.password();
            url = "jdbc:postgresql://" + dbCredentials.ip() + ":" + dbCredentials.port() + "/" + dbCredentials.dbName();
        } catch (ConfigFileNotFoundException e) {
            username = "root";
            password = "swain";
            url = "jdbc:postgresql://" + "localhost" + ":" + 5432 + "/" + "cryptobros_db";
        }
        this.username = username;
        this.password = password;
        this.url = url;
    }


    /**
     * Method that starts the inner connection to the database. Ideally, users would disconnect after
     * using the shared instance.
     */
    public Connection connect() {
        try {
            if (!conn.isClosed()) return conn;
            conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch(SQLException e) {
            // TODO: Handle errors with exceptions
            System.err.println("Couldn't connect to --> " + url + " (" + e.getMessage() + ")");
            return null;
        }
    }

    /**
     * Method that closes the inner connection to the database. Ideally, users would disconnect after
     * using the shared instance.
     */
    public void disconnect(){
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Problem when closing the connection --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
    }
}
