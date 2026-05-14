package org.cryptoBros.persistence;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.persistence.Exceptions.BotGenerationException;
import org.cryptoBros.persistence.Exceptions.CryptoNotAddedException;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

public interface AtomicPersistence {

    long createCryptoWithBot(Crypto crypto)
            throws DbConnectionException, BotGenerationException, CryptoNotAddedException;

    void deleteCryptoWithBot(String symbol)
            throws DbConnectionException, CryptoNotFoundException;

    void purchaseCryptoWithBalanceUpdate(long userId, String symbol, double currentPrice, double units, double totalCost)
            throws DbConnectionException, CryptoNotFoundException;
}