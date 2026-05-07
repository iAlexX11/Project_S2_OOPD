package org.cryptoBros.business;

import org.cryptoBros.persistence.BotPersistence;
import org.cryptoBros.persistence.SQL.BotSQL;
import org.cryptoBros.persistence.SQL.UserPortfolioSQL;
import org.cryptoBros.persistence.UserPortfolioPersistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton-style registry that creates and tears down bots
 * whenever the administrator adds or removes a cryptocurrency.
 */
public class BotManager {

    private final Map<String, Bot>  activeBots;
    private final BotPersistence    botDb;

    public BotManager() {
        this.botDb = new BotSQL();
        this.activeBots = new ConcurrentHashMap<>();
    }

    /**
     * Called immediately after a new Cryptocurrency row is inserted.
     * Creates the Users row, the Bots row, and starts the scheduler.
     */
    public void onCryptoCreated(String symbol, double volatility) throws Exception {
        // Persist bot user + bots table row, returns the new user_id
        int botUserId = botDb.createBotUser(symbol, volatility);

        // Build and start the in-memory bot
        Bot bot = new Bot(botUserId, symbol, volatility);
        activeBots.put(symbol, bot);
        bot.start();
    }

    /**
     * Called when a cryptocurrency is deleted.
     * DB cascades handle the rows; we just stop the scheduler.
     */
    public void onCryptoDeleted(String symbol) {
        Bot bot = activeBots.remove(symbol);
        if (bot != null) bot.stop();
    }
}