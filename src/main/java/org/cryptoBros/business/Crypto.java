package org.cryptoBros.business;

public class Crypto {
    private String name;
    private double currentPrice;
    private double initialPrice;

    public Crypto(String name, double currentPrice, double initialPrice) {
        this.name = name;
        this.currentPrice = currentPrice;
        this.initialPrice = initialPrice;
    }

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
