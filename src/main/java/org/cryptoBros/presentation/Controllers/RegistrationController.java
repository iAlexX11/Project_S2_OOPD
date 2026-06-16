package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.CredentialManager;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.*;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles user login and registration flows.
 */
public class RegistrationController implements ActionListener {

	private final FrameController frameController;
	private final LoginView loginView;
	private final SignUpView signUpView;
    private final UserController userController;
	private final AdminController adminController;
	private final CredentialManager credentialManager;

	/**
	 * Creates a new RegistrationController.
	 *
	 * @param frameController   the frame controller for displaying content
	 * @param initialController the initial controller for startup logic
	 */
	public RegistrationController (FrameController frameController, InitialController initialController) {
		this.loginView = new LoginView();
		this.signUpView = new SignUpView();
		this.frameController = frameController;
		CryptoManager cryptoManager = new CryptoManager();
		this.adminController = new AdminController(initialController, frameController, cryptoManager);
        this.userController = new UserController(frameController, initialController, cryptoManager);
		this.credentialManager = new CredentialManager();
		loginView.setActions(this);
		signUpView.setActions(this);
	}

	/**
	 * Shows the login view.
	 */
	public void displayLogin() {
		frameController.displayContent(loginView);
	}

	/**
	 * Shows the sign-up view.
	 */
	public void displaySignUp() {
		frameController.displayContent(signUpView);
	}

    private void signup (){
        try {
            userController.signUpLogic(signUpView.getEmail(), signUpView.getPassword(), signUpView.getConfirmPassword(), signUpView.getUsername());
            NavigatorController navigatorController = new NavigatorController(frameController, userController, adminController, false);
        } catch (UserNotAddException |UserAlreadyExistsException | CredentialsErrorFormatException e) {
            frameController.showError(e.getMessage());
        } catch (DbConnectionException ex) {
            //Never print this error in screen
        }
    }

    private void login () {
		if (loginView.getUsername().equals("admin")) {
			logInAdmin();
		} else {
			try {
				userController.logIn(loginView.getUsername(), loginView.getPassword());
				NavigatorController navigatorController = new NavigatorController(frameController, userController, adminController, false);
			} catch (UserNotFoundException | CredentialsErrorFormatException e) {
				frameController.showError(e.getMessage());
			} catch (DbConnectionException ex) {
				// NEVER PRINT THIS TYPE OF ERRORS IN SCREEN
			}
		}
    }

    private void logInAdmin() {
       try {
           String adminPassword = credentialManager.hashPassword(credentialManager.readAdminPassword().toCharArray());
           char[] password = loginView.getPassword();
           if (credentialManager.checkHashedPassword(password, adminPassword)) {
			   NavigatorController navigatorController = new NavigatorController(frameController, userController, adminController, true);
			   System.out.println("Admin logIn successfully");
           }
           else {
			   frameController.showError("This username or password are wrong!");
           }
       }catch (ConfigFileNotFoundException e) {
		   frameController.showError("The configuration File couldn't be found");
       }
    }

	/**
	 * Handles user actions from the login and sign-up views, dispatching
	 * to the appropriate registration or authentication flow.
	 *
	 * @param e the action event triggered by the user
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
		switch (buttonEnumeration) {
			case CONFIRM_SIGNUP -> signup();
			case LOGIN -> displayLogin();
			case CONFIRM_LOGIN -> login();
			case SIGNUP -> displaySignUp();
		}
	}
}