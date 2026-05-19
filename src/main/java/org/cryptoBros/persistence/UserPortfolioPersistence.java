package org.cryptoBros.persistence;

import org.cryptoBros.persistence.Exceptions.*;

import java.util.List;

public interface UserPortfolioPersistence {
    /**
     * Buys crypto for a user.
     * - If the user has no position: INSERT a new Portfolio row.
     * - If the user already holds this crypto: UPDATE by adding units to the existing row.
     * In both cases the DB trigger fires and raises current_price by 1%.
     * @param userId id of the user purchasing the crypto
     * @param cryptoSymbol symbol of the purchased crypto
     * @param currentPrice current price of the purchased crypto
     * @param units number of units bought
     * @throws PurchaseNotAddedException if there was an error inserting data
     * @throws DbConnectionException if there was a connection error with the db
     */
    void buyCrypto(long userId, String cryptoSymbol, double currentPrice, double units) throws PurchaseNotAddedException, DbConnectionException;

/**
 * Sells crypto for a user.
 * - Decreases units by the requested amount.
 * - If units reach 0 the Portfolio row is deleted (position closed).
 * The DB trigger fires on the UPDATE and lowers current_price by 1%.
 * @param userId id of the user selling the crypto
 * @param cryptoSymbol symbol of the sold crypto
 * @param units number of units to sell
 * @throws CryptoNotFoundException if no position is found for the crypto
 * @throws SaleNotAddedException if user attempts to sell more than what is owned, or an unexpected error happens
 * @throws DbConnectionException if there is a connection error with the db.
 */
    void sellCrypto(long userId, String cryptoSymbol, double units) throws CryptoNotFoundException, SaleNotAddedException, DbConnectionException;

    /**
     * Retrieves all portfolio positions for a user, including current crypto prices.
     * @param userId the user whose portfolio to retrieve
     * @return a list of positions (symbol, units, buyPrice, currentPrice); empty if user has no holdings
     * @throws DbConnectionException if there is a connection error with the db
     */
    List<PortfolioPosition> getUserPortfolio(long userId) throws DbConnectionException;
}
