package org.cryptoBros.business.Workers;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.business.Liseners.BotListener;
import org.cryptoBros.business.User;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.PurchaseNotAddedException;
import org.cryptoBros.persistence.Exceptions.SaleNotAddedException;
import org.cryptoBros.persistence.SQL.CryptoSQL;
import org.cryptoBros.persistence.SQL.UserPortfolioSQL;
import org.cryptoBros.persistence.UserPortfolioPersistence;
import org.cryptoBros.presentation.BotLogFormatter;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simulates market activity for a single cryptocurrency.
 *
 * Each tick (every 5/volatility seconds) the bot randomly buys or sells
 * 1 unit of its assigned crypto, using the same persistence layer as a
 * human user — so all DB triggers fire identically.
 */
public class Bot implements Runnable {

    private final Logger LOG;
    private static final double UNITS_PER_TRADE = 1.0;

    private final long                      botUserId;
    private final String                    cryptoSymbol;
    private final double                    volatility;
    private final Random                    rng;
    private final BotListener               botListener;

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?>       taskHandle;

    /**
     * Creates a new Bot for the given cryptocurrency.
     *
     * @param botUserId    the {@link User} id that was created for this bot
     * @param cryptoSymbol the {@link Crypto} this bot is responsible for
     * @param volatility   copied from {@link Crypto} at creation time
     * @param botListener  the callback listener for bot trading actions
     */
    public Bot(long botUserId, String cryptoSymbol, double volatility, BotListener botListener) {
        this.botUserId          = botUserId;
        this.cryptoSymbol       = cryptoSymbol;
        this.volatility         = volatility;
        this.portfolio          = new UserPortfolioSQL();
        this.rng                = new Random();
        this.cryptoPersistence  = new CryptoSQL();
        this.botListener      = botListener;
        this.LOG = Logger.getLogger(this.getClass().getName() + "-" + botUserId);
        this.LOG.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new BotLogFormatter());
        handler.setLevel(Level.ALL);
        this.LOG.addHandler(handler);
        this.LOG.setLevel(Level.ALL);
    }

    /**
     * Starts the periodic scheduler. Safe to call once per bot instance.
     */
    public void start() {
        if (scheduler != null && !scheduler.isShutdown()) {
            LOG.log(Level.WARNING, "Bot for " + cryptoSymbol + " is already running.");
            return;
        }

        // 5 / volatility seconds converted to milliseconds for precision
        long periodMs = (long) ((5.0 / volatility) * 1000);

        scheduler  = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "bot-" + cryptoSymbol);
            t.setDaemon(true); // won't prevent JVM shutdown
            return t;
        });

        taskHandle = scheduler.scheduleAtFixedRate(
                this, periodMs, periodMs, TimeUnit.MILLISECONDS);

        LOG.info(String.format(
                "Bot started for %s — acting every %.3f s (volatility=%.2f)",
                cryptoSymbol, periodMs / 1_000.0, volatility));
    }

    /**
     * Stops the bot and its scheduler. Safe to call multiple times.
     */
    public void stop() {
        if (taskHandle != null) taskHandle.cancel(false);
        if (scheduler  != null) scheduler.shutdown();
        LOG.info("Bot stopped for " + cryptoSymbol);
    }

    /**
     * Called by the scheduler on every tick.
     * 50 % chance of buy, 50 % chance of sell.
     */
    @Override
    public void run() {
        if (rng.nextBoolean()) {
            executeBuy();
        } else {
            executeSell();
        }
    }

    private void executeBuy() {
        try {
            botListener.onBotBuy(botUserId, cryptoSymbol, UNITS_PER_TRADE);
            LOG.info("Bot BUY  " + UNITS_PER_TRADE + " " + cryptoSymbol);

        } catch (PurchaseNotAddedException | DbConnectionException | CryptoNotFoundException  e) {
            LOG.log(Level.WARNING, "Bot buy failed for " + cryptoSymbol + e);
        }
    }

    private void executeSell() {
        try {
            botListener.onBotSell(botUserId, cryptoSymbol, UNITS_PER_TRADE);
            LOG.info("Bot SELL " + UNITS_PER_TRADE + " " + cryptoSymbol);

        } catch (CryptoNotFoundException e) {
            // Bot tried to sell crypto that didn't exist
            LOG.log(Level.WARNING, "Bot sell failed for " + cryptoSymbol, e);
        } catch (SaleNotAddedException e) {
            // Bot has no position in this crypto — treat it as a buy instead
            LOG.fine("Bot attempted to sell more than owned for " + cryptoSymbol + ", converting sell → buy");
            executeBuy();

        } catch (DbConnectionException e) {
            LOG.log(Level.WARNING, "Bot sell failed for " + cryptoSymbol + e);
        }
    }

    /**
     * Returns the crypto symbol this bot manages.
     *
     * @return the cryptocurrency ticker symbol
     */
    public String getCryptoSymbol() {
        return cryptoSymbol;
    }
}