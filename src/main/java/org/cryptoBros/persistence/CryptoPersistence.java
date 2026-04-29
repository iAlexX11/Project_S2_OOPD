package org.cryptoBros.persistence;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;

import java.util.List;

public interface CryptoPersistence {
    Crypto getCrypto(String name) throws CryptoNotFoundException;
    List<Crypto> getAllCrypto() throws CryptoNotFoundException;
    void addCrypto(Crypto newCrypto) throws CryptoNotFoundException;
    void removeCrypto(String name) throws CryptoNotFoundException;
    void updateCrypto(String name, Crypto newCrypto);
    void updatePrice(String name, double newPrice);
}
