package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.CredentialManager;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.business.Liseners.GraphPriceListener;
import org.cryptoBros.business.UserManager;
import org.cryptoBros.business.Workers.GraphPriceWorker;
import org.cryptoBros.persistence.Exceptions.*;
import org.cryptoBros.persistence.PortfolioPosition;
import org.cryptoBros.presentation.Views.DisplayMessage;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Mediates between views and business logic for user operations.
 */
public class UserController {

    private final InitialController initialController;
	private final FrameController frameController;
    private final CryptoManager cryptoManager;
    private final AccountManager accountManager;
	private final UserManager userManager;
	private final CredentialManager credentialManager;

	/**
	 * Creates a new UserController.
	 *
	 * @param frameController   the frame controller for displaying content
	 * @param initialController the initial controller for startup logic
	 * @param cryptoManager     the manager for cryptocurrency operations
	 */
	public UserController(FrameController frameController, InitialController initialController, CryptoManager cryptoManager) {
		this.frameController = frameController;
        this.initialController = initialController;
		this.credentialManager = new CredentialManager();
        this.userManager = new UserManager();
        this.cryptoManager = cryptoManager;
        this.accountManager = new AccountManager();
	}

	/**
	 * Returns the current user's balance.
	 *
	 * @return the balance, or 0 if an error occurs
	 */
	public double getBalance() {
		try {
			return userManager.getUserBalance(userManager.getCurrentUserId());
		} catch (UserNotFoundException ex) {
			frameController.showError(ex.getMessage());
		} catch (DbConnectionException ex) {
			// NEVER PRINT THIS EXCEPTIONS
		}
        return 0;
	}

    /**
     * Logs out the current user and returns to the welcome screen.
     */
    public void logout() {
        userManager.clearCurrentUser();
        userManager.stopBalanceScheduler();
        userManager.clearBalanceListener();
        initialController.startProgram();
    }

    /**
     * Deletes the current user account.
     */
    public void deleteUser() {
        try {
            AccountManager accountManager = new AccountManager();
            accountManager.deleteUser(userManager.getCurrentUserId());
            userManager.clearCurrentUser();
            logout();
        } catch (UserNotFoundException | DbConnectionException e) {
            DisplayMessage.showMessage(frameController.getMainFrame() ,e.getMessage());
        }
    }

    /**
     * Registers a new user with the given credentials.
     *
     * @param email             the user's email address
     * @param password          the user's password
     * @param confirmedPassword the password confirmation
     * @param username          the desired username
     * @throws DbConnectionException         if a database error occurs
     * @throws UserNotAddException            if the user could not be added
     * @throws UserAlreadyExistsException     if the user already exists
     * @throws CredentialsErrorFormatException if the credentials are invalid
     */
    public void signUpLogic(String email, char[] password, char[] confirmedPassword, String username) throws DbConnectionException, UserNotAddException, UserAlreadyExistsException, CredentialsErrorFormatException {
        int id = accountManager.signUpLogic(email, password, confirmedPassword, username);
        userManager.setCurrentUserId(id);
    }

    /**
     * Authenticates a user by username or email.
     *
     * @param usernameOrEmail the username or email
     * @param password        the user's password
     * @throws UserNotFoundException if the user is not found
     * @throws DbConnectionException if a database error occurs
     */
    public void logIn(String usernameOrEmail, char[] password) throws UserNotFoundException, DbConnectionException {
        int id = accountManager.logInNormalUser(usernameOrEmail, password);
        userManager.setCurrentUserId(id);
    }

    /**
     * Registers a listener for balance updates.
     *
     * @param listener the balance listener to register
     */
    public void registerBalanceListener(BalanceListener listener) {
        userManager.changeBalanceListener(listener);
    }

    /**
     * Registers a listener for crypto data updates.
     *
     * @param listener the crypto listener to register
     */
    public void registerCryptoListener(CryptoListener listener) {
        cryptoManager.addCryptoListener(listener);
    }

    /**
     * Removes the current crypto data listener.
     */
    public void removeCryptoListener() {
        cryptoManager.addCryptoListener((symbol,name, price, change, pct) -> {});
    }

    /**
     * Pushes the current balance to the given listener.
     *
     * @param listener the balance listener to notify
     */
    public void pushCurrentBalance(BalanceListener listener) {
        double balance = getBalance();

        listener.balanceChanged(balance);
    }

    /**
     * Starts the graph worker for a cryptocurrency.
     *
     * @param listener the graph price listener to receive updates
     * @param symbol   the cryptocurrency symbol
     */
    public void setGraphicWorker (GraphPriceListener listener, String symbol){
        cryptoManager.setGraphWorker(listener, symbol);
    }

    /**
     * Stops the graph price worker.
     */
    public void stopGraphWorker() {
        cryptoManager.stopGraphWorker();
    }

    /**
     * Adds funds to the current user's balance.
     *
     * @param addBalanceAmount the amount to add
     */
    public void addBalance(double addBalanceAmount) {
        try {
            userManager.addBalance(addBalanceAmount);
        } catch (UserNotFoundException | DbConnectionException e){
			DisplayMessage.showMessage(frameController.getMainFrame() ,e.getMessage());
        }
    }

    /**
     * Returns the current user's portfolio as table data.
     *
     * @return a two-dimensional array of portfolio rows
     */
    public Object[][] getPortfolioData() {
        try {
            List<PortfolioPosition> positions = cryptoManager.getUserPortfolio(userManager.getCurrentUserId());
            Object[][] data = new Object[positions.size()][4];
            for (int i = 0; i < positions.size(); i++) {
                PortfolioPosition pos = positions.get(i);
                double profit = (pos.currentPrice() - pos.buyPrice()) * pos.units();
                String profitStr = String.format("%s%.2f €", profit >= 0 ? "+" : "", profit);
                data[i] = new Object[]{
                        pos.cryptoSymbol(),
                        pos.units(),
                        String.format("%.2f €", pos.buyPrice()),
                        profitStr
                };
            }
            return data;
        } catch (DbConnectionException e) {
            return new Object[0][];
        }
    }

    /**
     * Calculates the total profit across all portfolio positions.
     *
     * @return the total profit, or 0.0 if an error occurs
     */
    public double getTotalProfit() {
        try {
            List<PortfolioPosition> positions = cryptoManager.getUserPortfolio(userManager.getCurrentUserId());
            return cryptoManager.calculateTotalProfit(positions);
        } catch (DbConnectionException e) {
            return 0.0;
        }
    }

	/**
	 * Changes the current user's password.
	 *
	 * @param password the new password
	 */
	public void changeUserPassword(char[] password) {
		try {
			userManager.changeUserPassword(credentialManager.hashPassword(password));
			DisplayMessage.showMessage(frameController.getMainFrame(), "The password has been changed!");
		} catch (DbConnectionException e) {
			DisplayMessage.showMessage(frameController.getMainFrame() ,e.getMessage());
		}
	}

	/**
	 * Changes the current user's username.
	 *
	 * @param username the new username
	 */
	public void changeUsername(String username) {
		try {
			userManager.changeUsername(username);
			DisplayMessage.showMessage(frameController.getMainFrame(), "The username has been changed!");
		} catch (DbConnectionException | UsernameAlreadyExists e) {
			DisplayMessage.showMessage(frameController.getMainFrame() ,e.getMessage());
		}
	}

    /**
     * Fetches all cryptocurrencies and notifies listeners.
     */
    public void getAllCrypto() {
        try {
            cryptoManager.getAllCrypto();
        } catch (DbConnectionException | CryptoNotFoundException  e) {
            DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
        }
    }

    /**
     * Loads initial cryptocurrency data and starts bots.
     */
    public void initCrypto() {
        try {
            cryptoManager.loadInitialCrypto();
        } catch (CryptoNotAddedException | FileNotFoundException | BotGenerationException | DbConnectionException e) {
            DisplayMessage.showMessage(frameController.getMainFrame() ,e.getMessage());
        }
    }

    /**
     * Returns the display name of a cryptocurrency.
     *
     * @param symbol the cryptocurrency symbol
     * @return the display name, or a placeholder if not found
     */
    public String getCryptoName(String symbol){
        try {
            return cryptoManager.getCryptoName(symbol);
        } catch (DbConnectionException | CryptoNotFoundException e) {
            return "<Crypto>";
        }
    }

    /**
     * Returns the units owned by the current user for a cryptocurrency.
     *
     * @param symbol the cryptocurrency symbol
     * @return the number of units owned, or 0 if an error occurs
     */
    public double getOwnedUnits(String symbol) {
        try {
            return cryptoManager.getOwnedUnits(symbol, userManager.getCurrentUserId());
        } catch (DbConnectionException e) {
            return 0;
        }
    }

    /**
     * Sells cryptocurrency units and credits the proceeds.
     *
     * @param symbol the cryptocurrency symbol
     * @param units  the number of units to sell
     */
    public void sellCrypto(String symbol, double units) {
        try {
            double proceeds = cryptoManager.sell(userManager.getCurrentUserId(), symbol, units);
            userManager.addBalance(proceeds);
        } catch (DbConnectionException | CryptoNotFoundException | SaleNotAddedException | UserNotFoundException e) {
            DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
        }
    }

    /**
     * Purchases cryptocurrency units and deducts the cost.
     *
     * @param currentSymbol the cryptocurrency symbol
     * @param units         the number of units to buy
     */
    public void buyCrypto(String currentSymbol, double units) {
        try {
           double totalCost = cryptoManager.purchase(userManager.getCurrentUserId(), currentSymbol, units);
           userManager.deductBalance(totalCost);
        } catch (DbConnectionException | CryptoNotFoundException | PurchaseNotAddedException | UserNotFoundException | InsufficientBalanceException e) {
            DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
        }
    }

    /**
     * Displays pending crypto-related notifications.
     */
    public void displayCryptoNotification() {
        try {
            List<String> messages = userManager.displayCryptoNotification();
            for (String message : messages) {
                DisplayMessage.showMessage(frameController.getMainFrame(), message);
            }
        } catch (DbConnectionException e) {
            DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
        }
    }
}
