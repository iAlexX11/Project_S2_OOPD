package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.ListenersPersistence.ViewListener;
import org.cryptoBros.presentation.Views.SettingsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingController implements ActionListener {

    private final FrameController frameController;
    private final SettingsView settingsView;
    ViewListener viewListener;

    public SettingController(FrameController frameController, ViewListener viewListener) {
        this.frameController = frameController;
        this.settingsView = new SettingsView();
        settingsView.setActions(this);
    }

    public void displaySettings() {
        frameController.displayContent(settingsView);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        viewListener.setAction(buttonEnumeration);
    }
}
