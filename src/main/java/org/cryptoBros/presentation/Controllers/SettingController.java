package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Views.SettingsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingController implements ActionListener, BalanceListener {

    private final FrameController frameController;
    Navigation pagesListeners;
    private final SettingsView settingsView;

    public SettingController(FrameController frameController) {
        this.frameController = frameController;
        this.pagesListeners = pagesListeners;
        this.settingsView = new SettingsView();
        settingsView.setActions(this);
    }

    public void displaySettings() {
        frameController.displayContent(settingsView);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        switch (buttonEnumeration) {
            case SETTINGS -> {}
            case HOME -> {
                CryptoMarketController cryptoMarketController = new CryptoMarketController(frameController);
            }
            case PORTFOLIO -> System.out.println("PORTFOLIO");
            case LOGOUT -> logout();
            case ACCOUNT -> System.out.println("Account");
            case DELETE -> System.out.println("Delete");
        }
    }

    @Override
    public void balanceChanged(double balance) {
        settingsView.updateBalance(balance);
    }
}
