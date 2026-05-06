package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.CredentialManager;
import org.cryptoBros.business.User;
import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
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

	private boolean signUpCredentialsFormat () {
		boolean emailOk = accountManager.checkEmail(signUpView.getEmail());
		boolean passwordOk = accountManager.checkPassword(signUpView.getPassword());
		boolean difPassword = Arrays.equals(signUpView.getPassword(), signUpView.getConfirmPassword());

		if (emailOk && passwordOk && difPassword) {
			return true;
		} else if (!emailOk && !passwordOk) {
			ErrorsView.showError("Wrong email and password format");
		} else if (!emailOk) {
			ErrorsView.showError("Wrong email format");
		} else if (!passwordOk) {
			ErrorsView.showError("Wrong password format");
		} else {
			ErrorsView.showError("Password must match");
		}
		return false;
	}

	public void registerNewUser() {
		UserManager userManager = new UserManager();
		if (signUpCredentialsFormat()) {
			String password = accountManager.hashPassword(signUpView.getPassword());
			String email = signUpView.getEmail();
			String username = signUpView.getUsername();

            try {
				userManager.getUser(username, email);
				ErrorsView.showError("Username/email already exists");
			} catch (UserNotFoundException e) {
				try {
					User user = new User(username, email, password);
					User userWithId = userManager.addUser(user);
					userManager.setCurrentUser(userWithId);
                    SettingController settingController = new SettingController(frameController);
                    settingController.displaySettings();
				} catch (UserNotAddException | DbConnectionException ex) {
					ErrorsView.showError(ex.getMessage());
				}
			}
			catch (DbConnectionException e) {
				ErrorsView.showError(e.getMessage());
			}
		}
	}

    private void logInUser() {
        String usernameOrEmail = loginView.getUsername();

        if (usernameOrEmail.compareTo("admin") == 0) {
            logInAdmin();
        }
        else {
            logInNormalUser(usernameOrEmail);
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
               ErrorsView.showError("This username or password are wrong!");
           }
       }catch (ConfigFileNotFoundException e) {
           ErrorsView.showError("The configuration File couldn't be found");
       }
    }

    public void logInNormalUser(String usernameOrEmail) {
		UserManager userManager = new UserManager();
        User possibleUser = null;
        try {
            possibleUser = userManager.getUser(usernameOrEmail, usernameOrEmail);
        } catch (UserNotFoundException | DbConnectionException e) {
			// TODO: improve login exception
            ErrorsView.showError(e.getMessage());
			return;
        }

        if (possibleUser != null && accountManager.checkHashedPassword(loginView.getPassword(), possibleUser.getPassword())) {
			System.out.println(possibleUser.getEmail());
			System.out.println(possibleUser.getUsername());
			System.out.println(possibleUser.getPassword());
			userManager.setCurrentUser(possibleUser);
            System.out.println("User logIn successfully");
			SettingController settingController = new SettingController(frameController);
			settingController.displaySettings();
			//TODO: Redirect to the main page
		} else {
			ErrorsView.showError("This email/username doesn't exists!");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
		switch (buttonEnumeration) {
			case CONFIRM_SIGNUP -> registerNewUser();
			case LOGIN -> login();
			case CONFIRM_LOGIN -> logInUser();
			case SIGNUP -> signUp();
		}
	}


}