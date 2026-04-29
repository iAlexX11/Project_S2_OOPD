package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.UserManager;
import org.cryptoBros.presentation.Views.ButtonEnumeration;
import org.cryptoBros.presentation.Views.CryptoMarketView;

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
		cryptoMarketView.setBalance(userManager.getUserBalance(username, email));
		frameController.displayContent(cryptoMarketView);
	}

	public double getUserBalance(String username, String email) {
		return userManager.getUserBalance(username, email);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
		switch (buttonEnumeration) {
			case SETTINGS -> ;
			case PORTFOLIO -> ;
			//TODO: The cryptos table
		}

		 */
	}
}
