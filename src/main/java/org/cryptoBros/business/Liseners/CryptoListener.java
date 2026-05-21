package org.cryptoBros.business.Liseners;

public interface CryptoListener{
    void updateData(String symbol, String name, double currentPrice, double change, double percentage);
}
