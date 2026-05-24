package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.PortfolioView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controls the portfolio view, handling balance deposits and sell operations.
 */
public class PortfolioController implements ActionListener, BalanceListener, CryptoListener {

    private final Navigation navigation;
    private final PortfolioView portfolioView;
    private final UserController userController;

    /**
     * Creates a new PortfolioController.
     *
     * @param userController the user controller for business operations
     * @param navigation     the navigation handler for page transitions
     */
    public PortfolioController(UserController userController,  Navigation navigation) {
        this.portfolioView = new PortfolioView();
        this.navigation = navigation;
        this.userController = userController;

        portfolioView.setActions(this);
        portfolioView.setSellCallback(this::onSellClicked);
    }

    private void onSellClicked(int row) {
        String symbol = portfolioView.getSymbolAt(row);
        double units = portfolioView.getUnitsAt(row);
        userController.sellCrypto(symbol, units);
        refreshPortfolioData();
    }

    private void refreshPortfolioData() {
        Object[][] data = userController.getPortfolioData();
        portfolioView.setPortfolioData(data);
        double totalProfit = userController.getTotalProfit();
        portfolioView.updateProfit(totalProfit);
    }

    /**
     * Handles user actions from the portfolio view, including navigation
     * and balance deposit confirmation.
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
            case CONFIRM -> addBalance();
        }
    }

    private void addBalance () {
        userController.addBalance(portfolioView.getAddBalanceAmount());
    }

    /**
     * Updates the displayed balance and estimated profit on the portfolio view
     * when the user balance changes.
     *
     * @param balance the new balance value
     */
    @Override
    public void balanceChanged(double balance) {
        portfolioView.updateBalance(balance);
        portfolioView.updateEstimatedProfit(userController.getTotalProfit());
    }

    /**
     * Refreshes the portfolio table and estimated profit when cryptocurrency
     * price data is updated.
     *
     * @param symbol       the ticker symbol of the updated cryptocurrency
     * @param name         the display name of the cryptocurrency
     * @param currentPrice the latest price of the cryptocurrency
     * @param change       the absolute price change
     * @param percentage   the percentage price change
     */
    @Override
    public void updateData(String symbol, String name, double currentPrice, double change, double percentage) {
        refreshPortfolioData();
        portfolioView.updateEstimatedProfit(userController.getTotalProfit());
    }

    /**
     * Refreshes portfolio data and returns the portfolio view.
     *
     * @return the portfolio view
     */
    public BaseView getView() {
        refreshPortfolioData();
        return portfolioView;
    }
}