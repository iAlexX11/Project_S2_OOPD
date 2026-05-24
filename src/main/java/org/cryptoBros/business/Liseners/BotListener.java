package org.cryptoBros.business.Liseners;

import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.PurchaseNotAddedException;
import org.cryptoBros.persistence.Exceptions.SaleNotAddedException;

/**
 * Callback interface for bot trading actions.
 */
public interface BotListener {
    /**
     * Called when the bot executes a buy order.
     *
     * @param botUserId    the bot user identifier
     * @param cryptoSymbol the cryptocurrency ticker symbol
     * @param units        the number of units to buy
     * @throws PurchaseNotAddedException if the purchase could not be recorded
     * @throws DbConnectionException    if the database connection fails
     * @throws CryptoNotFoundException  if the cryptocurrency is not found
     */
    void onBotBuy(long botUserId, String cryptoSymbol, double units) throws PurchaseNotAddedException, DbConnectionException, CryptoNotFoundException;

    /**
     * Called when the bot executes a sell order.
     *
     * @param botUserId    the bot user identifier
     * @param cryptoSymbol the cryptocurrency ticker symbol
     * @param units        the number of units to sell
     * @throws DbConnectionException   if the database connection fails
     * @throws SaleNotAddedException   if the sale could not be recorded
     * @throws CryptoNotFoundException if the cryptocurrency is not found
     */
    void onBotSell(long botUserId, String cryptoSymbol, double units) throws DbConnectionException, SaleNotAddedException, CryptoNotFoundException;
}
