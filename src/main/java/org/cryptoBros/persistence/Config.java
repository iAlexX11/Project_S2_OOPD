package org.cryptoBros.persistence;

/**
 * Holds application configuration values read from config.json.
 *
 * @param port the database port
 * @param ip the database host address
 * @param dbName the database name
 * @param username the database username
 * @param password the database password
 * @param adminPassword the admin password
 */
public record Config(int port, String ip, String dbName, String username, String password, String adminPassword) {
}