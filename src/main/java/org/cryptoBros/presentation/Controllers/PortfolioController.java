package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.PortfolioView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PortfolioController implements ActionListener, BalanceListener {

    private final Navigation navigation;
    private final PortfolioView portfolioView;
	private final UserController userController;
	private final AdminController adminController;

    public PortfolioController(UserController userController, Navigation navigation, AdminController adminController) {
		this.userController = userController;
		this.adminController = adminController;
		this.portfolioView = new PortfolioView();
        this.navigation = navigation;
        portfolioView.setActions(this);
    }

    // TODO refresh portfolio with information from the database
    private void refreshPortfolioData () {
        // This is an example
        Object[][] data = {
                { "LSCoin",  5,    "45,35 €",       "+328,70 €"      },
                { "Bitcoin", 2,    "1.536,52 €",    "+166.295,56 €"  },
                { "Tether",  1259, "23,78 €",       "-28.742,97 €"   },
                { "LSCoin",  27,   "259,01 €",      "-3.993,84 €"    },
                { "Bitcoin", 1,    "100.451,91 €",  "-15.767,61 €"   },
        };
        portfolioView.setPortfolioData(data);
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
        portfolioView.updateBalance(balance);
    }

    public BaseView getView() {
        return portfolioView;
    }
}