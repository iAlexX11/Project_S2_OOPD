package org.cryptoBros.business;

public class Crypto {
    private String symbol;
    private String name;
    private double currentPrice;
    private double initialPrice;

    public Crypto(String symbol, String name, double currentPrice, double initialPrice) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.initialPrice = initialPrice;
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
}
