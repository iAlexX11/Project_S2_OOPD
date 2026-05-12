package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.presentation.ListenersPersistence.PagesListeners;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.Views.CryptoMarketView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class CryptoMarketController implements ActionListener, BalanceListener, CryptoListener {

    private final FrameController frameController;
    private final CryptoMarketView cryptoMarketView;
    PagesListeners pagesListeners;

    public CryptoMarketController(FrameController frameController, PagesListeners pagesListeners) {
        this.frameController = frameController;
        this.cryptoMarketView = new CryptoMarketView();
        this.pagesListeners = pagesListeners;
        cryptoMarketView.setActions(this);
        updateData("Bitcoin", 12.32, -1.32, -0.02);
    }

    public void displayCryptoMarketView(double balance) {
        frameController.displayContent(cryptoMarketView);
        cryptoMarketView.updateBalance(balance);
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

    @Override
    public void updateData(String name, double currentPrice, double change, double percentage) {
        cryptoMarketView.updateCryptoTable(name, currentPrice, change, percentage);
    }
}
