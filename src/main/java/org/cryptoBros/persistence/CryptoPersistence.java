package org.cryptoBros.persistence;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.persistence.Exceptions.CryptoNotAddedException;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface CryptoPersistence {
    /**
     * Returns the crypto with the given name.
     *
     * @param name the crypto to be found
     * @return the crypto with the given name
     * @throws CryptoNotFoundException if the crypto is not found
     * @throws DbConnectionException if there is a problem with the database connection
     */
    Crypto getCrypto(String name) throws CryptoNotFoundException, DbConnectionException;

    /**
     * Returns all the cryptos in the database.
     * @return a list of all the cryptos in the database
     * @throws CryptoNotFoundException if there is no crypto to be found
     * @throws DbConnectionException if there is a problem with the database connection
     */
    List<Crypto> getAllCrypto() throws CryptoNotFoundException, DbConnectionException;

    /**
     * Adds a new crypto to the database.
     *
     * @param newCrypto the crypto to be added
     * @throws DbConnectionException if there is a problem with the database connection
     */
    void addCrypto(Crypto newCrypto) throws CryptoNotAddedException,DbConnectionException;

    /**
     * Removes a crypto from the database.
     *
     * @param name the name of the crypto to be removed
     * @throws CryptoNotFoundException if the crypto is not found
     * @throws DbConnectionException if there is a problem with the database connection
     */
    void removeCrypto(String name) throws CryptoNotFoundException, DbConnectionException;

    /**
     * Updates an existing crypto in the database.
     *
     * @param name the name of the crypto to be updated
     * @param newCrypto the new crypto data to be updated
     * @throws CryptoNotFoundException if the crypto is not found
     * @throws DbConnectionException if there is a problem with the database connection
     */
    void updateCrypto(String name, Crypto newCrypto) throws CryptoNotFoundException, DbConnectionException;

    /**
     * Updates the price of an existing crypto in the database.
     *
     * @param name the name of the crypto to be updated
     * @param newPrice the new price to be updated
     * @throws CryptoNotFoundException if the crypto is not found
     * @throws DbConnectionException if there is a problem with the database connection
     */
    void updatePrice(String name, double newPrice) throws CryptoNotFoundException, DbConnectionException;

    /**
     * Gets the price history of a crypto.
     * @param name the name of the crypto
     * @return a map of the price history of the crypto, where the key is the timestamp and the value is the price
     * @throws CryptoNotFoundException if the crypto is not found
     * @throws DbConnectionException if there is a problem with the database connection
     */
    Map<Instant, Double> getPriceHistory(String name) throws CryptoNotFoundException, DbConnectionException;
}
