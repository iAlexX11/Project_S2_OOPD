package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Views.ButtonEnumeration;
import org.cryptoBros.presentation.Views.SettingsView;
import org.cryptoBros.presentation.Views.WelcomeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingController implements ActionListener {

    private final FrameController frameController;
    private final SettingsView settingsView;

    public SettingController(FrameController frameController) {
        this.frameController = frameController;
        this.settingsView = new SettingsView();
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
            case BACK -> System.out.println("Back");
            case LOGOUT -> logout();
            case ACCOUNT -> System.out.println("Account");
            case DELETE -> System.out.println("Delete");
        }
    }
}
