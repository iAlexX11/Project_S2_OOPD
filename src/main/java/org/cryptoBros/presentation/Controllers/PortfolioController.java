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

public class PortfolioController implements ActionListener, BalanceListener, CryptoListener {

    private final Navigation navigation;
    private final PortfolioView portfolioView;
    private final UserController userController;

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

    @Override
    public void balanceChanged(double balance) {
        portfolioView.updateBalance(balance);
    }

    @Override
    public void updateData(String symbol, String name, double currentPrice, double change, double percentage) {
        refreshPortfolioData();
    }

    public BaseView getView() {
        refreshPortfolioData();
        return portfolioView;
    }
}