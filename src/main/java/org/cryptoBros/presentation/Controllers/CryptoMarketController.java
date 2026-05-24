package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.business.Liseners.GraphPriceListener;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.CryptoSelectedListener;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.CryptoMarketView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.Map;

/**
 * Controls the crypto market view, displaying live cryptocurrency data.
 */
public class CryptoMarketController implements ActionListener, BalanceListener, CryptoListener, CryptoSelectedListener {

    private final CryptoMarketView cryptoMarketView;
    private final Navigation navigation;
	private final UserController userController;
	private boolean isAdmin = false;

    /**
     * Creates a new CryptoMarketController.
     *
     * @param userController  the user controller for business operations
     * @param adminController the admin controller for admin operations
     * @param navigation      the navigation handler for page transitions
     * @param isAdmin         whether the current user is an admin
     */
    public CryptoMarketController(UserController userController, AdminController adminController, Navigation navigation, boolean isAdmin) {
        this.navigation = navigation;
		this.userController = userController;
		this.cryptoMarketView = new CryptoMarketView();
		cryptoMarketView.setTypeUser(isAdmin);
        cryptoMarketView.setActions(this);
        cryptoMarketView.setOnRowClick(this);
		this.isAdmin = isAdmin;
		if (!isAdmin) {
			userController.registerBalanceListener(this);
			userController.pushCurrentBalance(this);
		}

    }

    /**
     * Handles user actions from the crypto market view, routing navigation
     * to settings, home, portfolio, or manage crypto pages.
     *
     * @param e the action event triggered by the user
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        switch (buttonEnumeration) {
            case SETTINGS -> navigation.navigate(PagesName.SETTING);
            case HOME -> navigation.navigate(PagesName.CRYPTO_MARKET);
            case PORTFOLIO -> navigation.navigate(PagesName.PORTFOLIO);
			case MANAGE_CRYPTO -> navigation.navigate(PagesName.MANAGE_CRYPTO);
		}
    }

    /**
     * Updates the displayed balance and estimated profit on the market view
     * when the user balance changes.
     *
     * @param balance the new balance value
     */
    @Override
    public void balanceChanged(double balance) {
        cryptoMarketView.updateBalance(balance);
        cryptoMarketView.updateEstimatedProfit(userController.getTotalProfit());
    }

    /**
     * Updates the crypto table with the latest price data for a cryptocurrency
     * and refreshes the estimated profit for non-admin users.
     *
     * @param symbol       the ticker symbol of the cryptocurrency
     * @param name         the display name of the cryptocurrency
     * @param currentPrice the latest price of the cryptocurrency
     * @param change       the absolute price change
     * @param percentage   the percentage price change
     */
    @Override
    public void updateData(String symbol, String name, double currentPrice, double change, double percentage) {
        cryptoMarketView.updateCryptoTable(symbol, name, currentPrice, change, percentage);
		if (!isAdmin) cryptoMarketView.updateEstimatedProfit(userController.getTotalProfit());
    }

    /** Clears all rows from the crypto table. */
    public void clearTable() {
        cryptoMarketView.clearCryptoTable();
    }

    /**
     * Returns the crypto market view.
     * @return the crypto market view
     */
    public BaseView getView() {
        return cryptoMarketView;
    }

    /**
     * Navigates to the crypto detail view when a cryptocurrency row is selected
     * in the market table.
     *
     * @param type the ticker symbol of the selected cryptocurrency
     */
    @Override
    public void cryptoSelected(String type) {
        navigation.navigateToCryptoDetail(type);
    }
}
