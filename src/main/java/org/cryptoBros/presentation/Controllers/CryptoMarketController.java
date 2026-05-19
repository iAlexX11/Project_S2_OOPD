package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.business.User;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.CryptoMarketView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CryptoMarketController implements ActionListener, BalanceListener, CryptoListener {

    private final CryptoMarketView cryptoMarketView;
    private final Navigation navigation;
    private UserController userController;

    public CryptoMarketController(UserController userController, Navigation navigation) {
        this.navigation = navigation;
        this.cryptoMarketView = new CryptoMarketView();
        this.userController = userController;
        cryptoMarketView.setActions(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        switch (buttonEnumeration) {
            case SETTINGS -> navigation.navigate(PagesName.SETTING);
            case HOME -> navigation.navigate(PagesName.CRYPTO_MARKET);
            case PORTFOLIO -> navigation.navigate(PagesName.PORTFOLIO);
        }
    }

    @Override
    public void balanceChanged(double balance) {
        cryptoMarketView.updateBalance(balance);
    }

    @Override
    public void updateData(String name, double currentPrice, double change, double percentage) {
        cryptoMarketView.updateCryptoTable(name, currentPrice, change, percentage);
    }

    public void onDestroy() {
        userController.removeCryptoListener();
    }

    public void clearTable() {
        cryptoMarketView.clearCryptoTable();
    }

    public BaseView getView() {
        return cryptoMarketView;
    }
}
