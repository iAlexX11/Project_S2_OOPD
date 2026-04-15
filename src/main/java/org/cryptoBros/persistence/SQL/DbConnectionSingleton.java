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
    public void connect() {
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch(SQLException e) {
            System.err.println("Couldn't connect to --> " + url + " (" + e.getMessage() + ")");
        }
    }


    /**
     * Method that executes an insertion query to the connected database.
     *
     * @param query String representation of the query to execute.
     */
    public void insertQuery(String query){
        try {
            Statement s = conn.createStatement();
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(query);
            System.err.println("Problem when inserting --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
    }


    /**
     * Method that executes an update query to the connected database.
     *
     * @param query String representation of the query to execute.
     */
    public void updateQuery(String query){
        try {
            Statement s = conn.createStatement();
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(query);
            System.err.println("Problema when updating --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
    }


    /**
     * Method that executes a deletion query to the connected database.
     *
     * @param query String representation of the query to execute.
     */
    public void deleteQuery(String query){
        try {
            Statement s = conn.createStatement();
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(query);
            System.err.println("Problem when deleting --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }

    }


    /**
     * Method that executes a selection query to the connected database.
     *
     * @param query String representation of the query to execute.
     * @return The results of the selection.
     */
    public ResultSet selectQuery(String query){
        ResultSet rs = null;
        try {
            Statement s = conn.createStatement();
            rs = s.executeQuery(query);
        } catch (SQLException e) {
            System.err.println(query);
            System.err.println("Problem when selecting data --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
        return rs;
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
