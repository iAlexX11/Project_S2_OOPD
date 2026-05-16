package org.cryptoBros.business.Liseners;

public interface CryptoListener{

    void updateData(String name, double currentPrice, double change, double percentage);
}
