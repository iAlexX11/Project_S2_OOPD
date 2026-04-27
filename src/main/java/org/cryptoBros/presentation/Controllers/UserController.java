package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.UserManager;
import org.cryptoBros.presentation.Views.ButtonEnumeration;
import org.cryptoBros.presentation.Views.CryptoMarketView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserController implements ActionListener {

	private final FrameController frameController;
	private final CryptoMarketView cryptoMarketView;
	private final UserManager userManager;

	public UserController(FrameController frameController, UserManager userManager) {
		this.frameController = frameController;
		this.userManager = userManager;
		this.cryptoMarketView = new CryptoMarketView();
		cryptoMarketView.setActions(this);
	}

	public void displayCryptoMarketView() {
		cryptoMarketView.setBalance(getCurrentBalance());
		frameController.displayContent(cryptoMarketView);
	}

	private double getCurrentBalance() {
		return userManager.getCurrentBalance();
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
