package org.cryptoBros.business.Liseners;

import java.time.Instant;
import java.util.Map;

/** Notifies observers when price history data is available. */
public interface GraphPriceListener {
    /**
     * Called when graph price history is updated.
     *
     * @param cryptoHistory map of timestamps to prices
     */
    void updateGraph(Map<Instant, Double> cryptoHistory);
}
