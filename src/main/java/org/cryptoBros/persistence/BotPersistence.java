package org.cryptoBros.persistence;

import org.cryptoBros.persistence.Exceptions.BotGenerationException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;

public interface BotPersistence {

    int createBotUser(String cryptoSymbol, double volatility) throws BotGenerationException, DbConnectionException;
}