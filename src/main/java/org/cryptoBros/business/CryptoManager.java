package org.cryptoBros.business;

import org.cryptoBros.business.Liseners.BotListener;
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
import org.cryptoBros.persistence.UserPortfolioPersistence;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages cryptocurrency lifecycle, trading, and bot coordination.
 */
public class CryptoManager implements BotListener {

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
	 * @param cryptoName name of the crypto to be deleted
	 * @throws DbConnectionException   if there was a problem connecting to the {@link AtomicPersistence}
	 * @throws CryptoNotFoundException if the {@link Crypto} could not be found
	 */
    public void deleteCrypto(String cryptoName) throws
            DbConnectionException,
            CryptoNotFoundException
    {

        // DB succeeded: stop in-memory bot
        Bot bot = activeBots.remove(cryptoName);
        if (bot != null) bot.stop();
		atomicDb.deleteCryptoWithBot(cryptoName);
	}

    /**
     * Fetches all cryptocurrencies and notifies the listener for each.
     *
     * @throws DbConnectionException if the connection to the database fails
     * @throws CryptoNotFoundException if no cryptocurrencies are found
     */
    public void getAllCrypto() throws DbConnectionException, CryptoNotFoundException {
        List<Crypto> cryptos = cryptoPersistence.getAllCrypto();

        for (Crypto crypto : cryptos) {
            notifyListener(crypto);
        }
    }

    /**
     * Returns all cryptocurrencies as a list.
     *
     * @return a list of all cryptocurrencies
     * @throws CryptoNotFoundException if no cryptocurrencies are found
     * @throws DbConnectionException if the connection to the database fails
     */
    public List<Crypto> getAllCryptoList() throws CryptoNotFoundException, DbConnectionException {
        return cryptoPersistence.getAllCrypto();
    }

    /**
     * Sells crypto units for a user and returns the proceeds.
     *
     * @param userId the user selling the crypto
     * @param symbol the symbol of the crypto to sell
     * @param units the number of units to sell
     * @return the total proceeds from the sale
     * @throws DbConnectionException if the connection to the database fails
     * @throws SaleNotAddedException if the sale could not be completed
     * @throws CryptoNotFoundException if the crypto could not be found
     */
    public double sell(long userId, String symbol, double units) throws
            DbConnectionException,
            SaleNotAddedException,
            CryptoNotFoundException
    {
        double priceBeforeSell = cryptoPersistence.getCurrentPrice(symbol);
        double proceeds = priceBeforeSell * units;
        portfolioPersistence.sellCrypto(userId, symbol, units);
        Crypto updatedCrypto = cryptoPersistence.getCrypto(symbol);
        notifyListener(updatedCrypto);
        return proceeds;
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
     * @return the total cost of the purchase
     * @throws InsufficientBalanceException if the user does not have enough balance
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
        double currentPrice = cryptoPersistence.getCurrentPrice(symbol);

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

    /**
     * Registers a listener for cryptocurrency data updates.
     *
     * @param listener the listener to register
     */
    public void addCryptoListener(CryptoListener listener) {
        this.cryptoListener = listener;
    }

    /**
     * Retrieves the portfolio positions for a user.
     *
     * @param userId the ID of the user
     * @return a list of portfolio positions for the user
     * @throws DbConnectionException if the connection to the database fails
     */
    public List<PortfolioPosition> getUserPortfolio(long userId) throws DbConnectionException {
        return portfolioPersistence.getUserPortfolio(userId);
    }

    /**
     * Calculates the total profit across all portfolio positions.
     *
     * @param positions the list of portfolio positions to evaluate
     * @return the total profit amount
     */
    public double calculateTotalProfit(List<PortfolioPosition> positions) {
        double total = 0.0;
        for (PortfolioPosition pos : positions) {
            total += (pos.currentPrice() - pos.buyPrice()) * pos.units();
        }
        return total;
    }

    /**
     * Loads initial cryptocurrency data and starts bots.
     *
     * @throws CryptoNotAddedException if a crypto could not be added
     * @throws DbConnectionException if the connection to the database fails
     * @throws FileNotFoundException if the initial data file is not found
     * @throws BotGenerationException if a bot could not be generated
     */
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

    /**
     * Starts the graph price worker for a specific cryptocurrency.
     *
     * @param listener the listener to receive graph price updates
     * @param symbol the symbol of the cryptocurrency
     */
    public void setGraphWorker (GraphPriceListener listener, String symbol) {
        graphPriceWorker.start(listener, symbol);
    }

    /**
     * Returns the display name of a cryptocurrency by its symbol.
     *
     * @param symbol the symbol of the cryptocurrency
     * @return the display name of the cryptocurrency
     * @throws DbConnectionException if the connection to the database fails
     * @throws CryptoNotFoundException if the crypto could not be found
     */
    public String getCryptoName(String symbol) throws DbConnectionException, CryptoNotFoundException {
        return cryptoPersistence.getCrypto(symbol).getName();
    }

    /**
     * Stops the graph price worker.
     */
    public void stopGraphWorker() {
        graphPriceWorker.stop();
    }

    /**
     * Returns the number of units a user owns of a specific cryptocurrency.
     *
     * @param symbol the symbol of the cryptocurrency
     * @param currentUserId the ID of the current user
     * @return the number of units owned, or 0 if none
     * @throws DbConnectionException if the connection to the database fails
     */
    public double getOwnedUnits(String symbol, int currentUserId) throws DbConnectionException {
        List<PortfolioPosition> userPortfolio = getUserPortfolio(currentUserId);

        for (int i = 0; i < userPortfolio.size(); i++) {
            if (userPortfolio.get(i).cryptoSymbol().equals(symbol)) {
                return userPortfolio.get(i).units();
            }
        }
        return 0;
    }

	/**
	 * Renames a cryptocurrency if the new name is not already taken.
	 *
	 * @param oldName the current name of the cryptocurrency
	 * @param newName the new name to assign
	 * @throws ErrorChangingCryptoName if the rename operation fails
	 * @throws CryptoNameAlreadyExists if the new name is already in use
	 */
	public void changeCryptoName(String oldName, String newName)
			throws ErrorChangingCryptoName, CryptoNameAlreadyExists {
		try {
			List<Crypto> cryptos = cryptoPersistence.getAllCrypto();

			boolean alreadyExists = false;
			for (Crypto crypto : cryptos) {
				if (crypto.getName().equals(newName)) {
					alreadyExists = true;
					break;
				}
			}

			if (alreadyExists) {
				throw new CryptoNameAlreadyExists("This name already exists.");
			}
			cryptoPersistence.changeCryptoName(oldName, newName);

		} catch (CryptoNotFoundException | DbConnectionException e) {
			throw new ErrorChangingCryptoName("Something happened, please try again later.");
		}
	}

    /**
     * Handles a bot buy order by purchasing crypto at the current price and updating the UI.
     *
     * @param botUserId    the bot user identifier
     * @param cryptoSymbol the cryptocurrency ticker symbol to buy
     * @param units        the number of units to buy
     * @throws PurchaseNotAddedException if units are not greater than zero or the purchase could not be recorded
     * @throws DbConnectionException    if the database connection fails
     * @throws CryptoNotFoundException  if the cryptocurrency is not found
     */
    @Override
    public void onBotBuy(long botUserId, String cryptoSymbol, double units) throws
            PurchaseNotAddedException, DbConnectionException, CryptoNotFoundException
    {
        if (units <= 0)
            throw new PurchaseNotAddedException("Units must be greater than zero.");

        double currentPrice = cryptoPersistence.getCurrentPrice(cryptoSymbol);
        portfolioPersistence.buyCrypto(botUserId, cryptoSymbol, currentPrice, units);
        notifyListener(cryptoPersistence.getCrypto(cryptoSymbol));
    }

    /**
     * Handles a bot sell order by selling crypto units and updating the UI.
     *
     * @param botUserId    the bot user identifier
     * @param cryptoSymbol the cryptocurrency ticker symbol to sell
     * @param units        the number of units to sell
     * @throws DbConnectionException   if the database connection fails
     * @throws SaleNotAddedException   if the sale could not be recorded
     * @throws CryptoNotFoundException if the cryptocurrency is not found
     */
    @Override
    public void onBotSell(long botUserId, String cryptoSymbol, double units) throws
            DbConnectionException, SaleNotAddedException, CryptoNotFoundException
    {
        portfolioPersistence.sellCrypto(botUserId, cryptoSymbol, units);
        notifyListener(cryptoPersistence.getCrypto(cryptoSymbol));
    }

}
