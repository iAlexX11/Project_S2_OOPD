package org.cryptoBros.business;

import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.SQL.CryptoSQL;

public class CryptoManager {

    private CryptoListener cryptoListener;
    private CryptoPersistence cryptoPersistence;

    public CryptoManager() {
        this.cryptoListener = cryptoListener;
        this.cryptoPersistence = new CryptoSQL();
    }


    public void addCryptoListener(CryptoListener listener) {
        this.cryptoListener = listener;
    }
}
