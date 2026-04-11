package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Views.MainFrame;
import org.cryptoBros.presentation.Views.WelcomeView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialController implements ActionListener {

    MainFrame mainFrame;
    WelcomeView welcomeView;
    RegistrationController registrationController;

    public InitialController(MainFrame mainFrame) {
        this.registrationController = new RegistrationController(mainFrame);
        this.welcomeView = new WelcomeView();
        welcomeView.setActions(this);
        this.mainFrame = mainFrame;
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
}
