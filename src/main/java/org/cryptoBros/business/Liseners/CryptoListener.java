package org.cryptoBros.business.Liseners;

/** Notifies observers when cryptocurrency data changes. */
public interface CryptoListener{
    /**
     * Called when cryptocurrency data is updated.
     *
     * @param symbol       the ticker symbol
     * @param name         the display name
     * @param currentPrice the current market price
     * @param change       the absolute price change
     * @param percentage   the percentage price change
     */
    void updateData(String symbol, String name, double currentPrice, double change, double percentage);
}
