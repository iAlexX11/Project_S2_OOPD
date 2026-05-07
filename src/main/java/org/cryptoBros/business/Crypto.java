package org.cryptoBros.business;

import com.google.gson.annotations.SerializedName;

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

    public Crypto(String symbol, String name, double currentPrice, double initialPrice, double volatility) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.initialPrice = initialPrice;
        this.volatility = volatility;
    }

    public String getSymbol() { return symbol; }

    public String getName() {
        return name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public double getVolatility() { return volatility; }
}
