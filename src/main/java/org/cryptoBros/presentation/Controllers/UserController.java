package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.presentation.ListenersPersistence.PagesListeners;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.Views.Pages;

public class UserController implements PagesListeners {

    private final InitialController initialController;
	private final FrameController frameController;
	private final UserManager userManager;
    private final SettingController settingController;
    private final CryptoMarketController cryptoMarketController;


	public UserController(FrameController frameController, InitialController initialController) {
		this.frameController = frameController;
        this.settingController = new SettingController(frameController, this);
        this.userManager = new UserManager();
        this.cryptoMarketController = new CryptoMarketController(frameController, this);
        this.initialController = initialController;
	}

	public double getBalance() {
		try {
			return userManager.getUserBalance();
		} catch (UserNotFoundException ex) {
			frameController.showError(ex.getMessage());
		} catch (DbConnectionException ex) {
			// NEVER PRINT THIS EXCEPTIONS
		}
        return 0;
	}

    public void displayHome() {
        cryptoMarketController.displayCryptoMarketView(getBalance());
    }

    private void logout() {
        userManager.clearBalanceListener();
        userManager.clearCurrentUser();
        initialController.startProgram();
    }

    @Override
    public void setAction(ButtonEnumeration action) {
        switch (action) {
            case SETTINGS -> {
                settingController.displaySettings(100); //To be implement the get balance
                userManager.updateBalanceListener(settingController); // This is so he know how has to notify the change in balance
            }
            case HOME -> {
                cryptoMarketController.displayCryptoMarketView(100);
                userManager.updateBalanceListener(cryptoMarketController);
            }
            case PORTFOLIO -> System.out.println("PORTFOLIO");
            //TODO: The cryptos table
            case BACK -> System.out.println("Back");
            case LOGOUT -> logout();
        }
    }
}
