package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.BalanceListener;
import org.cryptoBros.presentation.Views.ButtonEnumeration;
import org.cryptoBros.presentation.Views.CryptoMarketView;
import org.cryptoBros.presentation.Views.SettingsView;
import org.cryptoBros.presentation.Views.WelcomeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingController implements ActionListener, BalanceListener {

    private final FrameController frameController;
    private final SettingsView settingsView;
	private final CryptoMarketView cryptoMarketView;

    public SettingController(FrameController frameController, CryptoMarketView cryptoMarketView) {
        this.frameController = frameController;
        this.settingsView = new SettingsView();
		this.cryptoMarketView = cryptoMarketView;
        settingsView.setActions(this);
    }

    public void displaySettings() {
        frameController.displayContent(settingsView);
    }

    private void logout() {
        InitialController initialController = new InitialController(frameController);
        initialController.startProgram();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        switch (buttonEnumeration) {
            case BACK -> frameController.displayContent(cryptoMarketView);
            case LOGOUT -> logout();
            case ACCOUNT -> System.out.println("Account");
            case DELETE -> System.out.println("Delete");
        }
    }
}
