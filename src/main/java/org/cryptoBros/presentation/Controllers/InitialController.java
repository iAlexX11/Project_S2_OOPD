package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.CredentialManager;
import org.cryptoBros.persistence.SQL.DbConnectionSingleton;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;
import org.cryptoBros.presentation.Views.WelcomeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialController implements ActionListener {

    private final FrameController frameController;
    private final WelcomeView welcomeView;
    private final RegistrationController registrationController;
	private final CredentialManager credentialManager;

    public InitialController(FrameController frameController) {
        this.registrationController = new RegistrationController(frameController);
        this.welcomeView = new WelcomeView();
        welcomeView.setActions(this);
        this.frameController = frameController;
        this.credentialManager = new CredentialManager();
    }

    public void startProgram() {
        frameController.displayContent(welcomeView);

        try {
            DbConnectionSingleton.getInstance().loadConfig();
        } catch (ConfigFileNotFoundException e) {
            frameController.showError("Config file not found");
            System.exit(1);
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
