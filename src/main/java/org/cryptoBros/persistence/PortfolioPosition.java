package org.cryptoBros.persistence;

public record PortfolioPosition(String cryptoSymbol, double units, double buyPrice, double currentPrice) {}
