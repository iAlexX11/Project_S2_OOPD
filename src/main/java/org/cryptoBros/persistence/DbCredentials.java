package org.cryptoBros.persistence;

public record DbCredentials(int port, String ip, String dbName, String username, String password) {
}