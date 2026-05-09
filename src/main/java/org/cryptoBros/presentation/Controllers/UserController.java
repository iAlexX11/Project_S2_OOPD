package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.presentation.ListenersPersistence.PagesListeners;
import org.cryptoBros.presentation.ButtonEnumeration;

public class UserController implements PagesListeners {

    private final InitialController initialController;
	private final FrameController frameController;
	private final UserManager userManager;
    private final SettingController settingController;
    private final CryptoMarketController cryptoMarketController;

    private String username;
    private String email;

	public UserController(FrameController frameController, InitialController initialController) {
		this.frameController = frameController;
        this.settingController = new SettingController(frameController, this);
        this.userManager = new UserManager();
        this.cryptoMarketController = new CryptoMarketController(frameController, this);
        this.initialController = initialController;

        userManager.addBalanceListener(cryptoMarketController);
        userManager.addBalanceListener(settingController);
	}

	public double getBalance(int id) {
		try {
			return userManager.getUserBalance(id);
		} catch (UserNotFoundException ex) {
			frameController.showError(ex.getMessage());
		} catch (DbConnectionException ex) {
			// NEVER PRINT THIS EXCEPTIONS
		}
        return 0;
	}

    public void displayHome(int id) {
        userManager.setCurrentUserId(id);
        userManager.addBalanceListener(cryptoMarketController);
        userManager.addBalanceListener(settingController);
        userManager.startBalanceScheduler();
        cryptoMarketController.displayCryptoMarketView(getBalance(userManager.getCurrentUserId()));
    }

    private void logout() {
        userManager.clearCurrentUser();
        userManager.stopBalanceScheduler();
        userManager.clearBalanceListener();
        initialController.startProgram();
    }

    @Override
    public void setAction(ButtonEnumeration action) {
        double currentBalance = getBalance(userManager.getCurrentUserId());
        switch (action) {
            case SETTINGS -> {
                settingController.displaySettings(currentBalance);
            }
            case HOME -> {
                cryptoMarketController.displayCryptoMarketView(currentBalance);
            }
            case PORTFOLIO -> System.out.println("PORTFOLIO");
            case BACK -> System.out.println("Back");
            case LOGOUT -> logout();
            case ACCOUNT -> System.out.println("Account");
            case DELETE -> System.out.println("Delete");
        }
    }
}
