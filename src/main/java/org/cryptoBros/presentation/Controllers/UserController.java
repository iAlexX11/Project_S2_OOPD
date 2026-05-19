package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.*;
import org.cryptoBros.presentation.Views.ErrorsView;

import java.io.FileNotFoundException;

public class UserController{

    private final InitialController initialController;
	private final FrameController frameController;
    private final CryptoManager cryptoManager;
    private final AccountManager accountManager;
	private final UserManager userManager;


	public UserController(FrameController frameController, InitialController initialController) {
		this.frameController = frameController;
        this.initialController = initialController;

        this.userManager = new UserManager();
        this.cryptoManager = new CryptoManager();
        this.accountManager = new AccountManager();
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

    public void logout() {
        userManager.clearCurrentUser();
        userManager.stopBalanceScheduler();
        userManager.clearBalanceListener();
        initialController.startProgram();
    }

    public void deleteUser() {
        try {
            AccountManager accountManager = new AccountManager();
            accountManager.deleteUser(userManager.getCurrentUserId());
            userManager.clearCurrentUser();
            logout();
        } catch (UserNotFoundException | DbConnectionException e) {
            ErrorsView.showError(frameController.getMainFrame() ,e.getMessage());
        }
    }

    public void signUpLogic(String email, char[] password, char[] confirmedPassword, String username) throws DbConnectionException, UserNotAddException, UserAlreadyExistsException, CredentialsErrorFormatException {
        int id = accountManager.signUpLogic(email, password, confirmedPassword, username);
        userManager.setCurrentUserId(id);
    }

    public void logIn(String usernameOrEmail, char[] password) throws UserNotFoundException, DbConnectionException {
        int id = accountManager.logInNormalUser(usernameOrEmail, password);
        userManager.setCurrentUserId(id);
    }

    public void registerBalanceListener(BalanceListener listener) {
        userManager.changeBalanceListener(listener);
    }

    public void registerCryptoListener(CryptoListener listener) {
        cryptoManager.addCryptoListener(listener);
    }

    public void removeCryptoListener() {
        cryptoManager.addCryptoListener((name, price, change, pct) -> {}); // no-op until next view registers
    }

    public void pushCurrentBalance(BalanceListener listener) {
        double balance = getBalance();

        listener.balanceChanged(balance);
    }

    public void addBalance(double addBalanceAmount) {
        try {
            userManager.addBalance(addBalanceAmount);
        } catch (UserNotFoundException | DbConnectionException e){
			ErrorsView.showError(frameController.getMainFrame() ,e.getMessage());
        }
    }

    public void getAllCrypto() {
        try {
            cryptoManager.getAllCrypto();
        } catch (DbConnectionException | CryptoNotFoundException  e) {
            ErrorsView.showError(frameController.getMainFrame(), e.getMessage());
        }
    }

    public void initCrypto() {
        try {
            cryptoManager.loadInitialCrypto();
        } catch (CryptoNotAddedException | FileNotFoundException | BotGenerationException | DbConnectionException e) {
            ErrorsView.showError(frameController.getMainFrame() ,e.getMessage());
        }
    }
}
