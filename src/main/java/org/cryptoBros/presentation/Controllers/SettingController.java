package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.BalanceListener;
import org.cryptoBros.presentation.ListenersPersistence.PagesListeners;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.Views.SettingsView;
import org.cryptoBros.presentation.Views.WelcomeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingController implements ActionListener, BalanceListener {

    private final FrameController frameController;
    PagesListeners pagesListeners;
    private final SettingsView settingsView;

    public SettingController(FrameController frameController, PagesListeners pagesListeners) {
        this.frameController = frameController;
        this.pagesListeners = pagesListeners;
        this.settingsView = new SettingsView();
        settingsView.setActions(this);
    }

    public void displaySettings(double currentBalance) {
        frameController.displayContent(settingsView);
        settingsView.updateBalance(currentBalance);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        pagesListeners.setAction(buttonEnumeration);
    }

    @Override
    public void balanceChanged(double balance) {
        settingsView.updateBalance(balance);
    }
}
