package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.presentation.Views.ButtonEnumeration;
import org.cryptoBros.presentation.Views.CryptoMarketView;
import org.cryptoBros.presentation.Views.ErrorsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserController implements ActionListener {

	private final FrameController frameController;
	private final CryptoMarketView cryptoMarketView;
	private final UserManager userManager = new UserManager();

	public UserController(FrameController frameController) {
		this.frameController = frameController;
		this.cryptoMarketView = new CryptoMarketView();
		cryptoMarketView.setActions(this);
	}

	public void displayCryptoMarketView(String username, String email) {
		//TODO: Get the balance of each user from the persistance, like "getBalanceOfUser(int userId)"
		try {
			cryptoMarketView.setBalance(userManager.getUserBalance(username, email));
		} catch (UserNotFoundException | DbConnectionException ex) {
			frameController.showError(ex.getMessage());
			frameController.displayContent(cryptoMarketView);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SettingController settingController = new SettingController(frameController);
		ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
		switch (buttonEnumeration) {
			case SETTINGS -> settingController.displaySettings();
			case PORTFOLIO -> System.out.println("PORTFOLIO");
			//TODO: The cryptos table
		}
	}
}
