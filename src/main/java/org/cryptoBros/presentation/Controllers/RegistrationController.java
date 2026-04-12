package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Views.LoginView;
import org.cryptoBros.presentation.Views.MainFrame;
import org.cryptoBros.presentation.Views.SignUpView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationController implements ActionListener {

    private MainFrame mainFrame;
    private LoginView loginView;
    private SignUpView signUpView;

    public RegistrationController (MainFrame mainFrame) {
        this.loginView = new LoginView();
        this.signUpView = new SignUpView();
        this.mainFrame = mainFrame;
    }

    public void login() {
        loginView.setActions(this);
        mainFrame.displayContent(loginView);
    }

    public void signUp() {
        signUpView.setActions(this);
        mainFrame.displayContent(signUpView);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "CONFIRM_SIGNUP" -> System.out.println("confirm signUp");
            case "GO_LOGIN" -> login();
            case "CONFIRM_LOGIN" ->  System.out.println("confirm login");
            case "GO_SIGNUP" -> signUp();
        }
    }
}
