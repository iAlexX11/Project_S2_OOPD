package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.ListenersPersistence.PagesListeners;
import org.cryptoBros.presentation.Views.PortfolioView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PortfolioController implements ActionListener, BalanceListener {

    private final FrameController frameController;
    private final PortfolioView portfolioView;
    PagesListeners pagesListeners;

    public PortfolioController(FrameController frameController, PagesListeners pagesListeners) {
        this.frameController = frameController;
        this.portfolioView = new PortfolioView();
        this.pagesListeners = pagesListeners;

        portfolioView.setActions(this);
    }

    public void displayPortfolioView(double balance) {
        frameController.displayContent(portfolioView);
        portfolioView.updateBalance(balance);
        refreshPortfolioData();
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
        String command = e.getActionCommand();
        //TODO: make a way to return data
        if (ButtonEnumeration.CONFIRM_BALANCE.name().equals(command)) {
            double amount = portfolioView.getAddBalanceAmount();
            if (amount > 0)
                pagesListeners.setAction(ButtonEnumeration.CONFIRM_BALANCE);
            return;
        }

        pagesListeners.setAction(ButtonEnumeration.valueOf(command));
    }

    @Override
    public void balanceChanged(double balance) {
        portfolioView.updateBalance(balance);
    }
}