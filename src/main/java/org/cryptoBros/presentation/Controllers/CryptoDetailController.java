package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.CryptoDetailView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CryptoDetailController implements ActionListener, BalanceListener {

    private final UserController userController;
    private final AdminController adminController;
    private final Navigation navigation;
    private final CryptoDetailView view;

    public CryptoDetailController (UserController userController, AdminController adminController, Navigation navigation, boolean isAdmin) {
        this.userController = userController;
        this.adminController = adminController;
        this.navigation = navigation;

        this.view = new CryptoDetailView();
        view.setTypeUser(isAdmin);
        view.setActions(this);
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
        view.updateBalance(balance);
    }

    public BaseView getView() {
        return view;
    }

    public void displayContent(String cryptoName) {
        view.setCryptoName(cryptoName);
    }
}
