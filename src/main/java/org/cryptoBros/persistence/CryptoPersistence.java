package org.cryptoBros.persistence;

import org.cryptoBros.business.Crypto;

import java.util.List;

public interface CryptoPersistence {
    Crypto getCrypto(String name);
    List<Crypto> getAllCrypto();
    void addCrypto(Crypto newCrypto);
    void removeCrypto(String name);
    void updateCrypto(String name, Crypto newCrypto);
    void updatePrice(String name, double newPrice);
}
