package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Views.SettingsView;
import org.cryptoBros.presentation.Views.WelcomeView;

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

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {


    }
}
