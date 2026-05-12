package org.cryptoBros.business;

import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.persistence.AtomicPersistence;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.Exceptions.BotGenerationException;
import org.cryptoBros.persistence.Exceptions.CryptoNotAddedException;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.SQL.AtomicSQL;
import org.cryptoBros.persistence.SQL.CryptoSQL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CryptoManager {

    private final CryptoListener cryptoListener;
    private final CryptoPersistence cryptoPersistence;
    private final Map<String, Bot> activeBots;
    private final AtomicPersistence atomicDb;

    /**
     * Builds a manager with its associated listener
     * @param cryptoListener the listener
     */
    public CryptoManager(CryptoListener cryptoListener) {
        this.cryptoListener = cryptoListener;
        this.cryptoPersistence = new CryptoSQL();
        this.activeBots = new ConcurrentHashMap<>();
        this.atomicDb = new AtomicSQL();
    }

    /**
     * Creates a crypto, adding it to the db and its associated bot
     * @param newCrypto the crypto to be created
     * @throws DbConnectionException if the db connection fails at any point during this process
     * @throws BotGenerationException if the bot could not be generated
     * @throws CryptoNotAddedException if the crypto could not be added to the db
     */
    public void createCrypto(Crypto newCrypto)
            throws DbConnectionException, BotGenerationException, CryptoNotAddedException {

        long botUserId = atomicDb.createCryptoWithBot(newCrypto);

        // DB succeeded: start in-memory bot
        Bot bot = new Bot(botUserId, newCrypto.getSymbol(), newCrypto.getVolatility());
        activeBots.put(newCrypto.getSymbol(), bot);
        bot.start();
    }

    /**
     * Deletes a crypto from the db, with its associated bot
     *
     * @param symbol symbol of the crypto to be deleted
     * @throws DbConnectionException if there was a problem connecting to the {@link AtomicPersistence}
     * @throws CryptoNotFoundException if the {@link Crypto} could not be found
     */
    public void deleteCrypto(String symbol) throws DbConnectionException, CryptoNotFoundException {
        // One transaction: crypto + bot user gone or neither is
        atomicDb.deleteCryptoWithBot(symbol);

        // DB succeeded: stop in-memory bot
        Bot bot = activeBots.remove(symbol);
        if (bot != null) bot.stop();
    }




}
