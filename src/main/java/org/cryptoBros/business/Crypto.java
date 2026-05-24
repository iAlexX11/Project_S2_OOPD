package org.cryptoBros.business;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a cryptocurrency with its market data.
 */
public class Crypto {
    @SerializedName("symbol")
    private String symbol;

    @SerializedName("name")
    private String name;

    @SerializedName("current_price")
    private double currentPrice;

    @SerializedName("original_price")
    private double initialPrice;

    @SerializedName("volatility")
    private double volatility;

    /**
     * Creates a new Crypto with the given market data.
     *
     * @param symbol       the ticker symbol
     * @param name         the display name
     * @param currentPrice the current market price
     * @param initialPrice the initial listing price
     * @param volatility   the volatility factor
     */
    public Crypto(String symbol, String name, double currentPrice, double initialPrice, double volatility) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.initialPrice = initialPrice;
        this.volatility = volatility;
    }

    /**
     * Returns the ticker symbol.
     * @return the ticker symbol
     */
    public String getSymbol() { return symbol; }

    /**
     * Returns the display name.
     * @return the display name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current market price.
     *
     * @return the current price
     */
    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Returns the initial listing price.
     *
     * @return the initial price
     */
    public double getInitialPrice() {
        return initialPrice;
    }

    /**
     * Returns the volatility factor.
     * @return the volatility factor
     */
    public double getVolatility() { return volatility; }
}
