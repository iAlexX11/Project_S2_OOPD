package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.*;

public class UserController{

    private final InitialController initialController;
	private final FrameController frameController;
    private final CryptoManager cryptoManager;
    private final AccountManager accountManager;
	private final UserManager userManager;

    private int id;

	public UserController(FrameController frameController, InitialController initialController) {
		this.frameController = frameController;
        this.initialController = initialController;

        this.userManager = new UserManager();
        this.cryptoManager = new CryptoManager();
        this.accountManager = new AccountManager(userManager);

        this.id = 0;
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

    public void logout() {
        userManager.clearCurrentUser();
        userManager.stopBalanceScheduler();
        userManager.clearBalanceListener();
        initialController.startProgram();
    }

    public void signUpLogic(String email, char[] password, char[] confirmedPassword, String username) throws DbConnectionException, UserNotAddException, UserAlreadyExistsException, CredentialsErrorFormatException {
            id = accountManager.signUpLogic(email, password, confirmedPassword, username);
    }

    public void logIn(String usernameOrEmail, char[] password) throws UserNotFoundException, DbConnectionException {
        id = accountManager.logInNormalUser(usernameOrEmail, password);
    }

    public void registerBalanceListener(BalanceListener listener) {
        userManager.changeBalanceListener(listener);
    }

    public void registerCryptoListener(CryptoListener listener) {
        cryptoManager.addCryptoListener(listener);
    }

    public void pushCurrentBalance(BalanceListener listener) {
        double balance = getBalance(id);
        listener.balanceChanged(balance);
    }
}
