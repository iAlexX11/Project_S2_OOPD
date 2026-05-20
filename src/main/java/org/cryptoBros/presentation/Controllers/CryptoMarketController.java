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

public class CryptoMarketController implements ActionListener, BalanceListener, CryptoListener, CryptoSelectedListener {

    private final CryptoMarketView cryptoMarketView;
    private final Navigation navigation;
	private final UserController userController;
	private final AdminController adminController;

    public CryptoMarketController(UserController userController, AdminController adminController, Navigation navigation, boolean isAdmin) {
        this.navigation = navigation;
		this.userController = userController;
		this.adminController = adminController;
		this.cryptoMarketView = new CryptoMarketView();
		cryptoMarketView.setTypeUser(isAdmin);
        cryptoMarketView.setActions(this);
        cryptoMarketView.setOnRowClick(this);
		if (!isAdmin) {
			userController.registerBalanceListener(this);
			userController.pushCurrentBalance(this);
		}

    }

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

    @Override
    public void balanceChanged(double balance) {
        cryptoMarketView.updateBalance(balance);
    }

    @Override
    public void updateData(String symbol, String name, double currentPrice, double change, double percentage) {
        cryptoMarketView.updateCryptoTable(symbol, name, currentPrice, change, percentage);
    }

    public void clearTable() {
        cryptoMarketView.clearCryptoTable();
    }

    public BaseView getView() {
        return cryptoMarketView;
    }

    @Override
    public void cryptoSelected(String type) {
        navigation.navigateToCryptoDetail(type);
    }
}
