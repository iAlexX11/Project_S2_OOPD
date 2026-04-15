package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Views.ButtonEnumeration;
import org.cryptoBros.presentation.Views.WelcomeView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialController implements ActionListener {

    private FrameController frameController;
    private WelcomeView welcomeView;
    private RegistrationController registrationController;

    public InitialController(FrameController frameController) {
        this.registrationController = new RegistrationController(frameController);
        this.welcomeView = new WelcomeView();
        welcomeView.setActions(this);
        this.frameController = frameController;
    }

    public void startProgram() {
        frameController.displayContent(welcomeView);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        switch (buttonEnumeration) {
            case LOGIN -> registrationController.login();
            case SIGNUP -> registrationController.signUp();
        }
    }

    private void loadAdminPassword() {
        try {
            // TODO: store the admin password somewhere
            credentialManager.readAdminPassword();
        } catch (ConfigFileNotFoundException e) {
            // TODO: implement alert thing for this
            System.out.println("Admin Password File Not Found");
        }
    }
}
