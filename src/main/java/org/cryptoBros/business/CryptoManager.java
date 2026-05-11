package org.cryptoBros.business;

import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.persistence.BotPersistence;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.Exceptions.BotGenerationException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.SQL.BotSQL;
import org.cryptoBros.persistence.SQL.CryptoSQL;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CryptoManager {

    private CryptoListener cryptoListener;
    private CryptoPersistence cryptoPersistence;
    private final Map<String, Bot> activeBots;
    private final BotPersistence botDb;

    /**
     * Builds a manager with its associated listener
     * @param cryptoListener the listener
     */
    public CryptoManager(CryptoListener cryptoListener) {
        this.cryptoListener = cryptoListener;
        this.cryptoPersistence = new CryptoSQL();
        this.activeBots = new ConcurrentHashMap<>();
        this.botDb = new BotSQL();
    }

    /**
     * Creates a crypto, adding it to the db and its associated bot
     * @param newCrypto the crypto to be created
     * @throws DbConnectionException if the db connection fails at any point during this process
     * @throws BotGenerationException if the bot could not be generated
     */
    public void createCrypto(Crypto newCrypto) throws DbConnectionException, BotGenerationException {
        // Persist bot user + bots table row, returns the new user_id
        int botUserId = botDb.createBotUser(newCrypto.getSymbol(), newCrypto.getVolatility());

        // Build and start the in-memory bot
        Bot bot = new Bot(botUserId, newCrypto.getSymbol(), newCrypto.getVolatility());
        activeBots.put(newCrypto.getSymbol(), bot);
        bot.start();

        // TODO: add the new crypto to the DB
    }

    public void deleteCrypto(String symbol) {
        Bot bot = activeBots.remove(symbol);
        if (bot != null) bot.stop();

        // TODO: remove the crypto from the db
    }




}
