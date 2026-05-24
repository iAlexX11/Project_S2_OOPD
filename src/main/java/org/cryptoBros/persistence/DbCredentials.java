package org.cryptoBros.persistence;

/**
 * Holds database connection credentials.
 *
 * @param port the database port
 * @param ip the database host address
 * @param dbName the database name
 * @param username the database username
 * @param password the database password
 */
public record DbCredentials(int port, String ip, String dbName, String username, String password) {
}