package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Views.ButtonEnumeration;
import org.cryptoBros.presentation.Views.LoginView;
import org.cryptoBros.presentation.Views.MainFrame;
import org.cryptoBros.presentation.Views.SignUpView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationController implements ActionListener {

    private FrameController frameController;
    private LoginView loginView;
    private SignUpView signUpView;

    public RegistrationController (FrameController frameController) {
        this.loginView = new LoginView();
        this.signUpView = new SignUpView();
        this.frameController = frameController;
        loginView.setActions(this);
        signUpView.setActions(this);
    }

    public void login() {
        frameController.displayContent(loginView);
    }

    public void signUp() {
        frameController.displayContent(signUpView);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        switch (buttonEnumeration) {
            case CONFIRM_SIGNUP -> System.out.println("confirm signUp");
            case LOGIN -> login();
            case CONFIRM_LOGIN ->  System.out.println("confirm login");
            case SIGNUP -> signUp();
        }
    }
}
