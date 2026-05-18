package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.AdminManager;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.business.UserManager;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;

import java.awt.event.ActionListener;

public class AdminController {

	private final InitialController initialController;
	private final FrameController frameController;
	private final CryptoManager cryptoManager;
	private final AccountManager accountManager;
	private final AdminManager adminManager;

	public AdminController(InitialController initialController, FrameController frameController) {
		this.frameController = frameController;
		this.initialController = initialController;

		this.adminManager = new AdminManager();
		this.cryptoManager = new CryptoManager();
		this.accountManager = new AccountManager();
	}

	public void adminLogout() {
		initialController.startProgram();
	}

	public void registerCryptoListener(CryptoListener listener) {
		cryptoManager.addCryptoListener(listener);
	}

}