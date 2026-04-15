package org.cryptoBros.persistence.SQL;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.persistence.CryptoPersistence;

import java.util.List;

/**
 * This class
 */

public class CryptoSQL implements CryptoPersistence {

    @Override
    public Crypto getCrypto(String name) {
        return null;
    }

    @Override
    public List<Crypto> getAllCrypto() {
        return List.of();
    }

    @Override
    public void addCrypto(Crypto newCrypto) {

    }

    @Override
    public void removeCrypto(String name) {

    }

    @Override
    public void updateCrypto(String name, Crypto newCrypto) {

    }

    @Override
    public void updatePrice(String name, double newPrice) {

    }
}
