package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.CredentialManager;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.persistence.SQL.DbConnectionSingleton;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.persistence.Exceptions.ConfigFileCorruptedException;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;
import org.cryptoBros.presentation.Views.DisplayMessage;
import org.cryptoBros.presentation.Views.WelcomeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Handles application startup, database initialization, and initial navigation.
 */
public class InitialController implements ActionListener {

    private final FrameController frameController;
    private final WelcomeView welcomeView;
    private final RegistrationController registrationController;
    private final CredentialManager credentialManager;

    /**
     * Creates a new InitialController.
     *
     * @param frameController the frame controller used for view management
     */
    public InitialController(FrameController frameController) {
        this.registrationController = new RegistrationController(frameController, this);
        this.welcomeView = new WelcomeView();
        welcomeView.setActions(this);
        this.frameController = frameController;
        this.credentialManager = new CredentialManager();
    }

    /**
     * Starts the application by loading configuration and verifying database connectivity.
     */
    public void startProgram() {
        frameController.displayContent(welcomeView);

        try {
            DbConnectionSingleton.getInstance().loadConfig();
        } catch (ConfigFileNotFoundException | ConfigFileCorruptedException e) {
            DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
            System.exit(1);
        }
        try {
            DbConnectionSingleton.getInstance().connect().close();
        } catch (SQLException e) {
            DisplayMessage.showMessage(frameController.getMainFrame(),
                    "Could not connect to the database. Please check your credentials.\n" + e.getMessage());
            System.exit(1);
        }

        try {
            credentialManager.readAdminPassword();
        } catch (ConfigFileNotFoundException e) {
            DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
        }
    }

    /**
     * Handles user actions from the welcome view, routing to the login
     * or sign-up screens.
     *
     * @param e the action event triggered by the user
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        switch (buttonEnumeration) {
            case LOGIN -> registrationController.displayLogin();
            case SIGNUP -> registrationController.displaySignUp();
        }
    }
}
