package org.cryptoBros.persistence;

import org.cryptoBros.business.Bot;
import org.cryptoBros.business.Crypto;
import org.cryptoBros.persistence.Exceptions.BotGenerationException;
import org.cryptoBros.persistence.Exceptions.CryptoNotAddedException;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

import java.io.FileNotFoundException;
import java.util.List;

public interface AtomicPersistence {

    long createCryptoWithBot(Crypto crypto)
            throws DbConnectionException, BotGenerationException, CryptoNotAddedException;

    void deleteCryptoWithBot(String symbol)
            throws DbConnectionException, CryptoNotFoundException;

    List<Bot> loadInitialData()
            throws CryptoNotAddedException, DbConnectionException, FileNotFoundException, BotGenerationException;

}