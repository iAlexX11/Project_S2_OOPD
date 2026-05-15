package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.CredentialManager;
import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.presentation.ListenersPersistence.PagesListeners;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.Views.ErrorsView;

import java.lang.reflect.AccessFlag;

public class UserController implements PagesListeners {

    private final InitialController initialController;
	private final FrameController frameController;
    private final CryptoManager cryptoManager;
	private final UserManager userManager;
    private final SettingController settingController;
    private final CryptoMarketController cryptoMarketController;
    private final PortfolioController portfolioController;

    private String username;
    private String email;

	public UserController(FrameController frameController, InitialController initialController) {
        this.frameController = frameController;
        this.settingController = new SettingController(frameController, this);
        this.cryptoMarketController = new CryptoMarketController(frameController, this);
        this.portfolioController = new PortfolioController(frameController, this);
        this.initialController = initialController;

        this.userManager =  new UserManager();
        this.cryptoManager = new CryptoManager(cryptoMarketController);

        userManager.addBalanceListener(cryptoMarketController);
        userManager.addBalanceListener(settingController);
        userManager.addBalanceListener(portfolioController);
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
        userManager.clearBalanceListener();
        userManager.clearCurrentUser();
        userManager.stopBalanceScheduler();
        userManager.clearBalanceListener();
        initialController.startProgram();
    }

    private void deleteUser() {
        try {
            AccountManager accountManager = new AccountManager(userManager);
            accountManager.deleteUser(userManager.getCurrentUserId());
            logout();
        } catch (UserNotFoundException | DbConnectionException e) {
            ErrorsView.showError(frameController.getMainFram() ,e.getMessage());
        }
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
            case PORTFOLIO -> {
                portfolioController.displayPortfolioView(100);
                userManager.updateBalanceListener(portfolioController);

            }
            //TODO: The cryptos table
            case LOGOUT -> logout();
            case ACCOUNT -> System.out.println("Account");
            case DELETE -> deleteUser();
        }
    }
}
