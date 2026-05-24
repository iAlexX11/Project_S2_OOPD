package org.cryptoBros.business.Workers;

import org.cryptoBros.business.Liseners.GraphPriceListener;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Periodically fetches and broadcasts price history for a cryptocurrency.
 */
public class GraphPriceWorker implements Runnable {
    private static final long UPDATE_INTERVAL_MS = 5000;

    private GraphPriceListener graphPriceListener;
    private final CryptoPersistence cryptoPersistence;
    private String cryptoSymbol;
    private Thread workerThread;
    private final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Creates a new GraphPriceWorker.
     *
     * @param persistence the persistence layer used to fetch price history
     */
    public GraphPriceWorker(CryptoPersistence persistence) {
        this.cryptoPersistence = persistence;
    }

    /**
     * Starts polling price history for the given cryptocurrency.
     *
     * @param listener the listener to notify with updated price data
     * @param symbol the cryptocurrency symbol to poll
     */
    public void start(GraphPriceListener listener, String symbol) {
        stop();

        this.graphPriceListener = listener;
        this.cryptoSymbol = symbol;

        workerThread = new Thread(this, "graph-worker-" + symbol);
        workerThread.setDaemon(true);
        workerThread.start();
    }

    /**
     * Stops the price history polling thread.
     */
    public void stop() {
        if (workerThread != null && workerThread.isAlive()) {
            workerThread.interrupt();
            workerThread = null;
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                graphPriceListener.updateGraph(
                        cryptoPersistence.getPriceHistory(cryptoSymbol)
                );
                logger.log(Level.FINE, "Graph update Succeeded");
                Thread.sleep(UPDATE_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (CryptoNotFoundException | DbConnectionException e) {
                logger.log(Level.SEVERE, "Graph update failed: " + e.getMessage());
            }
        }
    }
}