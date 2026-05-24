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

public interface AtomicPersistence {

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

    List<Bot> loadInitialData(CryptoManager cryptoManager, boolean seedFromJson)
            throws CryptoNotAddedException, DbConnectionException, FileNotFoundException, BotGenerationException;

    List<String> popNotifications(long userId)
        throws DbConnectionException;

}
