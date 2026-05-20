package org.cryptoBros.business.Workers;

import org.cryptoBros.business.Liseners.GraphPriceListener;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphPriceWorker implements Runnable {
    private static final long UPDATE_INTERVAL_MS = 5000;

    private final GraphPriceListener graphPriceListener;
    private final CryptoPersistence cryptoPersistence;
    private final String cryptoSymbol;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public GraphPriceWorker(GraphPriceListener listener,
                            CryptoPersistence persistence,
                            String symbol) {
        this.graphPriceListener = listener;
        this.cryptoPersistence = persistence;
        this.cryptoSymbol = symbol;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                graphPriceListener.updateGraph(
                        cryptoPersistence.getPriceHistory(cryptoSymbol)
                );
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