package org.cryptoBros.persistence;

/**
 * Represents a user's position in a cryptocurrency.
 *
 * @param cryptoSymbol the ticker symbol of the cryptocurrency
 * @param units        the number of units held
 * @param buyPrice     the price at which the units were purchased
 * @param currentPrice the current market price of the cryptocurrency
 */
public record PortfolioPosition(String cryptoSymbol, double units, double buyPrice, double currentPrice) {}
