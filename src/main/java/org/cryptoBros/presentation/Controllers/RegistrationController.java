package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
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
		if (signUpCredentialsFormat()) {
			String password = accountManager.hashPassword(signUpView.getPassword());
			String email = signUpView.getEmail();
			String userName = signUpView.getName();

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
		switch (buttonEnumeration) {
			case CONFIRM_SIGNUP -> registerNewUser();
			case LOGIN -> login();
			case CONFIRM_LOGIN -> System.out.println("User logged correctly");
			case SIGNUP -> signUp();
		}
	}
}