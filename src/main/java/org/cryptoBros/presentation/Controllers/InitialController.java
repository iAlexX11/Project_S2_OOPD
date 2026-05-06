package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.CredentialManager;
import org.cryptoBros.persistence.Exceptions.ConfigFileCorruptedException;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.Views.ErrorsView;
import org.cryptoBros.presentation.Views.WelcomeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialController implements ActionListener {

    private final FrameController frameController;
    private final WelcomeView welcomeView;
    private final RegistrationController registrationController;
    private final CredentialManager credentialManager;

    public InitialController(FrameController frameController) {
        this.registrationController = new RegistrationController(frameController, this);
        this.welcomeView = new WelcomeView();
        welcomeView.setActions(this);
        this.frameController = frameController;
        this.credentialManager = new CredentialManager();
    }

    public void startProgram() {
        frameController.displayContent(welcomeView);

        try {
            credentialManager.loadConfigFile();
        } catch (ConfigFileNotFoundException | ConfigFileCorruptedException e) {
            ErrorsView.showError(e.getMessage());
            System.exit(1);
        }
        try {
            credentialManager.readAdminPassword();
        } catch (ConfigFileNotFoundException e) {
            ErrorsView.showError("Admin Password File Not Found");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        switch (buttonEnumeration) {
            case LOGIN -> registrationController.login();
            case SIGNUP -> registrationController.signUp();
        }
    }
}
