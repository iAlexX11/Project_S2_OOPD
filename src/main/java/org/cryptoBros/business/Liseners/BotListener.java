package org.cryptoBros.business.Liseners;

import org.cryptoBros.persistence.Exceptions.CryptoNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.PurchaseNotAddedException;
import org.cryptoBros.persistence.Exceptions.SaleNotAddedException;

public interface BotListener {
    void onBotBuy(long botUserId, String cryptoSymbol, double units) throws PurchaseNotAddedException, DbConnectionException, CryptoNotFoundException;
    void onBotSell(long botUserId, String cryptoSymbol, double units) throws DbConnectionException, SaleNotAddedException, CryptoNotFoundException;
}
