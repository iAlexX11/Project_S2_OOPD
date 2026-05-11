package org.cryptoBros.business;

import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.SQL.CryptoSQL;

public class CryptoManager {

    CryptoListener cryptoListener;
    CryptoPersistence cryptoPersistence;

    public CryptoManager() {
        this.cryptoListener = cryptoListener;
        this.cryptoPersistence = new CryptoSQL();
    }


}
