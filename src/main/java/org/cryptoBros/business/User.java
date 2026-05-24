package org.cryptoBros.business;

/**
 * Represents a user of the CryptoBros platform.
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private double balance;

    /**
     * Creates a User with all fields.
     *
     * @param id       the user id
     * @param username the username
     * @param email    the email address
     * @param password the hashed password
     * @param balance  the account balance
     */
    public User(int id, String username, String email, String password, double balance) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    /**
     * Creates a new User with a default balance of 1000.
     *
     * @param username the username
     * @param email    the email address
     * @param password the hashed password
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
		this.balance = 1000;
    }

    /**
     * Returns the user id.
     * @return the user id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user id.
     * @param id the user id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the email address.
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the hashed password.
     * @return the hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the account balance.
     * @return the account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the account balance.
     * @param balance the new balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
