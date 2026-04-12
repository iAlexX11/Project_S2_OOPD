package org.cryptoBros.persistence;

public record Config(int port, String ip, String dbName, String username, String password, String adminPassword) {
}
