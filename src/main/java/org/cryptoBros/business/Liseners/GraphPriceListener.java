package org.cryptoBros.business.Liseners;

import java.time.Instant;
import java.util.Map;

public interface GraphPriceListener {
    void updateGraph(Map<Instant, Double> cryptoHistory);
}
