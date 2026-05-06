package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.presentation.Views.ButtonEnumeration;
import org.cryptoBros.presentation.Views.CryptoMarketView;
import org.cryptoBros.presentation.Views.ErrorsView;
import org.cryptoBros.presentation.Views.Pages;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserController implements ActionListener {

	private final FrameController frameController;
	private final CryptoMarketView cryptoMarketView;
	private final UserManager userManager = new UserManager();
	private Pages currentPage;

	public UserController(FrameController frameController) {
		this.frameController = frameController;
		this.cryptoMarketView = new CryptoMarketView();
		cryptoMarketView.setActions(this);
	}

	public void updateBalance(String username, String email) {
		try {
			currentPage.updateBalance(userManager.getUserBalance(username, email));
		} catch (UserNotFoundException ex) {
			frameController.showError(ex.getMessage());
		} catch (DbConnectionException ex) {
			// NEVER PRINT THIS EXCEPTIONS
		}
	}

	public void displayCryptoMarketView(String username, String email) {
		currentPage = cryptoMarketView;
		updateBalance(username, email);
		frameController.displayContent(cryptoMarketView);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SettingController settingController = new SettingController(frameController, cryptoMarketView);
		ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
		switch (buttonEnumeration) {
			case SETTINGS -> {
				settingController.displaySettings();
				userManager.updateBalanceListener(settingController);
			}
			case PORTFOLIO -> System.out.println("PORTFOLIO");
			//TODO: The cryptos table
		}
	}
}
