package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.BalanceListener;
import org.cryptoBros.presentation.ListenersPersistence.PagesListeners;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.Views.CryptoMarketView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CryptoMarketController implements ActionListener, BalanceListener {

    private final FrameController frameController;
    private final CryptoMarketView cryptoMarketView;
    PagesListeners pagesListeners;

    public CryptoMarketController(FrameController frameController, PagesListeners pagesListeners) {
        this.frameController = frameController;
        this.cryptoMarketView = new CryptoMarketView();
        this.pagesListeners = pagesListeners;
        cryptoMarketView.setActions(this);
    }

    public void displayCryptoMarketView(boolean isAdmin, double balance) {
		cryptoMarketView.setTypeUser(isAdmin);
        frameController.displayContent(cryptoMarketView);
        if (!isAdmin) {cryptoMarketView.updateBalance(balance);}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        pagesListeners.setAction(buttonEnumeration);
    }

    @Override
    public void balanceChanged(double balance) {
        cryptoMarketView.updateBalance(balance);
    }
}
