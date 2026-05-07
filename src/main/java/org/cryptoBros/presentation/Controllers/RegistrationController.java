package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.CredentialManager;
import org.cryptoBros.business.User;
import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.*;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.Views.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class RegistrationController implements ActionListener {

	private final FrameController frameController;
	private final LoginView loginView;
	private final SignUpView signUpView;
	private final AccountManager accountManager;
    private final InitialController initialController;
	private final UserManager userManager;

	public RegistrationController (FrameController frameController, InitialController initialController) {
		this.loginView = new LoginView();
		this.signUpView = new SignUpView();
		this.frameController = frameController;
		this.userManager = new UserManager();
		this.accountManager = new AccountManager(this.userManager);
        this.initialController = initialController;
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
		try {
			accountManager.signUpLogic(signUpView.getEmail(), signUpView.getPassword(), signUpView.getConfirmPassword(), signUpView.getUsername());
			UserController userController = new UserController(frameController);
		} catch (UserNotAddException | UserAlreadyExistsException | CredentialsErrorFormatException e) {
			frameController.showError(e.getMessage());
		} catch (DbConnectionException ex) {
			// NEVER PRINT THIS TYPE OF ERRORS IN SCREEN
		}
	}

    private void logInUser() {
        String usernameOrEmail = loginView.getUsername();

        if (usernameOrEmail.compareTo("admin") == 0) {
            logInAdmin();
        } else {
			try {
				User user = accountManager.logInNormalUser(usernameOrEmail, loginView.getPassword());
				UserController userController = new UserController(frameController);
			} catch (UserNotFoundException | CredentialsErrorFormatException e) {
				frameController.showError(e.getMessage());
			} catch (DbConnectionException ex) {
				// NEVER PRINT THIS TYPE OF ERRORS IN SCREEN
			}

        }
    }

    private void logInAdmin() {
        CredentialManager credentialManager = new CredentialManager();
        AccountManager accountManager = new AccountManager(userManager);
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