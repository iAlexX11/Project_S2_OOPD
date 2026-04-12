package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.CredentialManager;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;
import org.cryptoBros.presentation.Views.MainFrame;
import org.cryptoBros.presentation.Views.WelcomeView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialController implements ActionListener {

    MainFrame mainFrame;
    WelcomeView welcomeView;
    RegistrationController registrationController;
    private final CredentialManager credentialManager;

    public InitialController(MainFrame mainFrame) {
        this.registrationController = new RegistrationController(mainFrame);
        this.welcomeView = new WelcomeView();
        welcomeView.setActions(this);
        this.mainFrame = mainFrame;
        this.credentialManager = new CredentialManager();
    }

    public void startProgram() {
        mainFrame.displayContent(welcomeView);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "LOGIN"  -> registrationController.login();
            case "SIGNUP" -> registrationController.signUp();
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
