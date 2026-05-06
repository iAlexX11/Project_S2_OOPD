package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.presentation.ListenersPersistence.PagesListeners;
import org.cryptoBros.presentation.Views.ButtonEnumeration;
import java.awt.*;

public class UserController implements PagesListeners {

	private final FrameController frameController;
	private final UserManager userManager;
    private final SettingController settingController;
    private final CryptoMarketController cryptoMarketController;

    private String username;
    private String email;

	public UserController(FrameController frameController) {
		this.frameController = frameController;
        this.settingController = new SettingController(frameController, this);
        this.userManager = new UserManager();
        this.cryptoMarketController = new CryptoMarketController(frameController, this);
        this.email = email;
        this.username = username;
        cryptoMarketController.displayCryptoMarketView(getBalance(username, email));
	}

	public double getBalance(String username, String email) {
		try {
			return userManager.getUserBalance(username, email);
		} catch (UserNotFoundException ex) {
			frameController.showError(ex.getMessage());
		} catch (DbConnectionException ex) {
			// NEVER PRINT THIS EXCEPTIONS
		}
        return 0;
	}

    private void logout() {
        InitialController initialController = new InitialController(frameController);
        initialController.startProgram();
    }

    @Override
    public void setAction(ButtonEnumeration action) {
        switch (action) {
            case SETTINGS -> {
                settingController.displaySettings(100); //To be implement the get balance
                userManager.updateBalanceListener(settingController);
            }
            case PORTFOLIO -> System.out.println("PORTFOLIO");
            //TODO: The cryptos table
            case BACK -> System.out.println("Back");
            case LOGOUT -> logout();
            case ACCOUNT -> System.out.println("Account");
            case DELETE -> System.out.println("Delete");
        }
    }
}
