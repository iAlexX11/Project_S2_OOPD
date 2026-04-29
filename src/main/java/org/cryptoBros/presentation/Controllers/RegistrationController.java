package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.CredentialManager;
import org.cryptoBros.business.User;
import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;
import org.cryptoBros.persistence.SQL.UserSQL;
import org.cryptoBros.persistence.UserPersistence;
import org.cryptoBros.presentation.Views.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class RegistrationController implements ActionListener {

	private final FrameController frameController;
	private final LoginView loginView;
	private final SignUpView signUpView;
	private final AccountManager accountManager;

	public RegistrationController (FrameController frameController) {
		this.loginView = new LoginView();
		this.signUpView = new SignUpView();
		this.frameController = frameController;
		this.accountManager = new AccountManager();
		loginView.setActions(this);
		signUpView.setActions(this);
	}

	public void login() {
		frameController.displayContent(loginView);
	}

	public void signUp() {
		frameController.displayContent(signUpView);
	}

	public void signUpLogic() {
		String credentials = accountManager.signUpLogic(signUpView.getEmail(), signUpView.getPassword(), signUpView.getConfirmPassword(), signUpView.getUsername());
		if (!credentials.equals("ok")) {
			frameController.showError(credentials);
		} else {
			UserController userController = new UserController(frameController);
			userController.displayCryptoMarketView(signUpView.getUsername(), signUpView.getEmail());
		}
	}

    private void logInUser() {
        String usernameOrEmail = loginView.getUsername();

        if (usernameOrEmail.compareTo("admin") == 0) {
            logInAdmin();
        } else {
            String error = accountManager.logInNormalUser(usernameOrEmail, loginView.getPassword());
			if (error.equals("ok")) {
				UserController userController = new UserController(frameController);
				userController.displayCryptoMarketView(usernameOrEmail, usernameOrEmail);
			} else {
				frameController.showError(error);
			}
        }
    }

    private void logInAdmin() {
        CredentialManager credentialManager = new CredentialManager();
        AccountManager accountManager = new AccountManager();
       try {
           String adminPassword = accountManager.hashPassword(credentialManager.readAdminPassword().toCharArray());
           char[] password = loginView.getPassword();
           if (accountManager.checkHashedPassword(password, adminPassword)) {
               System.out.println("Admin logIn successfully");
           }
           else {
			   frameController.showError("This username or password are wrong!");
           }
       }catch (ConfigFileNotFoundException e) {
		   frameController.showError("The configuration File couldn't be found");
       }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
		switch (buttonEnumeration) {
			case CONFIRM_SIGNUP -> signUpLogic();
			case LOGIN -> login();
			case CONFIRM_LOGIN -> logInUser();
			case SIGNUP -> signUp();
		}
	}


}