package org.cryptoBros.business;

import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.business.Liseners.GraphPriceListener;
import org.cryptoBros.business.Workers.Bot;
import org.cryptoBros.business.Workers.GraphPriceWorker;
import org.cryptoBros.persistence.AtomicPersistence;
import org.cryptoBros.persistence.CryptoPersistence;
import org.cryptoBros.persistence.Exceptions.*;
import org.cryptoBros.persistence.PortfolioPosition;
import org.cryptoBros.persistence.SQL.AtomicSQL;
import org.cryptoBros.persistence.SQL.CryptoSQL;
import org.cryptoBros.persistence.SQL.UserPortfolioSQL;
import org.cryptoBros.persistence.SQL.UserSQL;
import org.cryptoBros.persistence.UserPersistence;
import org.cryptoBros.persistence.UserPortfolioPersistence;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CryptoManager {

    private CryptoListener cryptoListener;
    private CryptoPersistence cryptoPersistence;
    private final Map<String, Bot> activeBots;
    private final AtomicPersistence atomicDb;
    private final UserPortfolioPersistence portfolioPersistence;
    private final UserManager userManager;
    private final GraphPriceWorker graphPriceWorker;

    /**
     * Builds a manager with its associated listener
     */
    public CryptoManager() {
        this.cryptoPersistence = new CryptoSQL();
        this.activeBots = new ConcurrentHashMap<>();
        this.atomicDb = new AtomicSQL();
        this.portfolioPersistence = new UserPortfolioSQL();
        this.userManager = new UserManager();
        this.graphPriceWorker = new GraphPriceWorker(cryptoPersistence);
    }

    /**
     * Creates a crypto, adding it to the db and its associated bot
     * @param newCrypto the crypto to be created
     * @throws DbConnectionException if the db connection fails at any point during this process
     * @throws BotGenerationException if the bot could not be generated
     * @throws CryptoNotAddedException if the crypto could not be added to the db
     */
    public void createCrypto(Crypto newCrypto)
            throws DbConnectionException, BotGenerationException, CryptoNotAddedException {

        long botUserId = atomicDb.createCryptoWithBot(newCrypto);

        // DB succeeded: start in-memory bot
        Bot bot = new Bot(botUserId, newCrypto.getSymbol(), newCrypto.getVolatility(), this);
        activeBots.put(newCrypto.getSymbol(), bot);
        bot.start();
    }

    /**
     * Deletes a crypto from the db, with its associated bot
     *
     * @param symbol symbol of the crypto to be deleted
     * @return map of userId to refundAmount for each refunded holder
     * @throws DbConnectionException if there was a problem connecting to the {@link AtomicPersistence}
     * @throws CryptoNotFoundException if the {@link Crypto} could not be found
     */
    public Map<Long, Double> deleteCrypto(String symbol) throws
            DbConnectionException,
            CryptoNotFoundException
    {
        // One transaction: crypto + bot user gone or neither is
        Map<Long, Double> refunds = atomicDb.deleteCryptoWithBot(symbol);

        // DB succeeded: stop in-memory bot
        Bot bot = activeBots.remove(symbol);
        if (bot != null) bot.stop();

        return refunds;
    }

    public void getAllCrypto() throws DbConnectionException, CryptoNotFoundException {
        List<Crypto> cryptos = cryptoPersistence.getAllCrypto();

        for (Crypto crypto : cryptos) {
            notifyListener(crypto);
        }
    }

    public List<Crypto> getAllCryptoList() throws CryptoNotFoundException, DbConnectionException {
        return cryptoPersistence.getAllCrypto();
    }

    public void sell(long userId, String symbol, double units) throws
            DbConnectionException,
            SaleNotAddedException,
            CryptoNotFoundException
    {
        portfolioPersistence.sellCrypto(userId, symbol, units);
        Crypto updatedCrypto = cryptoPersistence.getCrypto(symbol);
        notifyListener(updatedCrypto);
    }


    /**
     * Purchases a crypto, orchestrating business logic related to it and returns the total to be removed
     * @param userId the user buying the crypto
     * @param symbol the crypto to be bought
     * @param units the amount of crypto being bought
     * @throws DbConnectionException if the connection to the db fails
     * @throws CryptoNotFoundException if the {@link Crypto} could not be found
     * @throws PurchaseNotAddedException if for internal reasons the purchase was not completed
     * @throws UserNotFoundException if the {@link User} could not be found
     */
    public double purchase(long userId, String symbol, double units) throws
            DbConnectionException,
            CryptoNotFoundException,
            PurchaseNotAddedException,
            UserNotFoundException, InsufficientBalanceException {
        if (units <= 0) {
            throw new PurchaseNotAddedException("Units to purchase must be greater than zero.");
        }

        // fetch crypto's current price
        double currentPrice = cryptoPersistence.getCrypto(symbol).getCurrentPrice();

        // fetch user balance
        double userBalance = userManager.getUserBalance(userId);
        double totalCost = currentPrice * units;

        if (totalCost > userBalance) {
            throw new PurchaseNotAddedException("You don't have enough money to purchase this crypto.");
        }

        // if price found and user has enough balance -> execute purchase
        portfolioPersistence.buyCrypto(userId, symbol, currentPrice, units);
        Crypto updatedCrypto = cryptoPersistence.getCrypto(symbol);
        notifyListener(updatedCrypto);

        return totalCost;
    }

    public void botPurchase(long userId, String symbol, double units) throws
            DbConnectionException, CryptoNotFoundException, PurchaseNotAddedException {

        if (units <= 0)
            throw new PurchaseNotAddedException("Units must be greater than zero.");

        double currentPrice = cryptoPersistence.getCrypto(symbol).getCurrentPrice();
        portfolioPersistence.buyCrypto(userId, symbol, currentPrice, units);
        notifyListener(cryptoPersistence.getCrypto(symbol));
    }

    private void notifyListener(Crypto crypto) {
        if (cryptoListener == null) return;
        double current = crypto.getCurrentPrice();
        double initial = crypto.getInitialPrice();
        SwingUtilities.invokeLater(() ->
                cryptoListener.updateData(
                        crypto.getSymbol(),
                        crypto.getName(),
                        current,
                        current - initial,
                        ((current - initial) / initial) * 100  // ← correct percentage
                )
        );
    }

    public void botSell(long userId, String symbol, double units) throws
            DbConnectionException, CryptoNotFoundException, SaleNotAddedException {

        portfolioPersistence.sellCrypto(userId, symbol, units);
        notifyListener(cryptoPersistence.getCrypto(symbol));
    }

    public void addCryptoListener(CryptoListener listener) {
        this.cryptoListener = listener;
    }

    public List<PortfolioPosition> getUserPortfolio(long userId) throws DbConnectionException {
        return portfolioPersistence.getUserPortfolio(userId);
    }

    public double calculateTotalProfit(List<PortfolioPosition> positions) {
        double total = 0.0;
        for (PortfolioPosition pos : positions) {
            total += (pos.currentPrice() - pos.buyPrice()) * pos.units();
        }
        return total;
    }

    public void loadInitialCrypto()
            throws CryptoNotAddedException, DbConnectionException, FileNotFoundException, BotGenerationException {

        boolean dbEmpty;
        try {
            cryptoPersistence.getAllCrypto();
            dbEmpty = false;
        } catch (CryptoNotFoundException e) {
            dbEmpty = true;
        }

        List<Bot> bots = atomicDb.loadInitialData(this, dbEmpty);

        for (Bot bot : bots) {
            if (activeBots.putIfAbsent(bot.getCryptoSymbol(), bot) == null) {
                bot.start();
            }
        }
    }

    public void setGraphWorker (GraphPriceListener listener, String symbol) {
        graphPriceWorker.start(listener, symbol);
    }

    public String getCryptoName(String symbol) throws DbConnectionException, CryptoNotFoundException {
        return cryptoPersistence.getCrypto(symbol).getName();
    }

    public void stopGraphWorker() {
        graphPriceWorker.stop();
    }

    public double getOwnedUnits(String symbol, int currentUserId) throws DbConnectionException {
        List<PortfolioPosition> userPortfolio = getUserPortfolio(currentUserId);

        for (int i = 0; i < userPortfolio.size(); i++) {
            if (userPortfolio.get(i).cryptoSymbol().equals(symbol)) {
                return userPortfolio.get(i).units();
            }
        }
        return 0;
    }

	public void changeCryptoName(String symbol, String name)
			throws ErrorChangingCryptoName, CryptoNameAlreadyExists {
		try {
			List<Crypto> cryptos = cryptoPersistence.getAllCrypto();

			boolean alreadyExists = false;
			for (Crypto crypto : cryptos) {
				if (crypto.getSymbol().equals(name)) {
					alreadyExists = true;
					break;
				}
			}

			if (alreadyExists) {
				throw new CryptoNameAlreadyExists("This name already exists.");
			}

			cryptoPersistence.changeCryptoName(symbol, name);

		} catch (CryptoNotFoundException | DbConnectionException e) {
			throw new ErrorChangingCryptoName("Something happened, please try again later.");
		}
	}
}
