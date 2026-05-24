package org.cryptoBros.persistence;

import org.cryptoBros.business.Workers.Bot;
import org.cryptoBros.business.Crypto;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.persistence.Exceptions.BotGenerationException;
import org.cryptoBros.persistence.Exceptions.CryptoNotAddedException;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Defines atomic database operations for crypto and bot management.
 */
public interface AtomicPersistence {

    /**
     * Atomically creates a cryptocurrency entry and its associated bot user.
     * @param crypto the cryptocurrency to create
     * @return the generated bot user ID
     * @throws DbConnectionException if the db connection fails
     * @throws BotGenerationException if the bot user cannot be created
     * @throws CryptoNotAddedException if the crypto entry cannot be inserted
     */
    long createCryptoWithBot(Crypto crypto)
            throws DbConnectionException, BotGenerationException, CryptoNotAddedException;

    /**
     * Deletes a cryptocurrency and its bot atomically, refunding all holders first.
     * Each holder's balance is increased by currentPrice * units before the cascade delete.
     * @param symbol the crypto to delete
     * @return map of userId to refundAmount for each refunded holder
     * @throws DbConnectionException if the db connection fails
     * @throws CryptoNotFoundException if the crypto is not found
     */
    Map<Long, Double> deleteCryptoWithBot(String symbol)
            throws DbConnectionException, CryptoNotFoundException;

    /**
     * Loads initial crypto data and creates bots from the seed file.
     * @param cryptoManager the crypto manager used to initialize bots
     * @param seedFromJson whether to seed data from the JSON file
     * @return list of bots loaded from the database
     * @throws CryptoNotAddedException if a crypto entry cannot be inserted
     * @throws DbConnectionException if the db connection fails
     * @throws FileNotFoundException if the seed file is not found
     * @throws BotGenerationException if a bot cannot be created
     */
    List<Bot> loadInitialData(CryptoManager cryptoManager, boolean seedFromJson)
            throws CryptoNotAddedException, DbConnectionException, FileNotFoundException, BotGenerationException;

    /**
     * Retrieves and deletes pending notifications for a user.
     * @param userId the ID of the user whose notifications to retrieve
     * @return list of notification messages
     * @throws DbConnectionException if the db connection fails
     */
    List<String> popNotifications(long userId)
        throws DbConnectionException;

}
