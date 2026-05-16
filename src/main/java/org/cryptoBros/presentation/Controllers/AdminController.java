package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AdminManager;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.ListenersPersistence.PagesListeners;

public class AdminController implements PagesListeners {

	private final AdminManager adminManager;
	private final CryptoMarketController cryptoMarketController;
	private final InitialController initialController;
	private final FrameController frameController;
	private final SettingController settingController;
	private final CryptoManager cryptoManager;
	private final ManageCryptoController manageCryptoController;

	public AdminController(FrameController frameController, InitialController initialController) {
		this.frameController = frameController;
		this.settingController = new SettingController(frameController, this);
		this.cryptoMarketController = new CryptoMarketController(frameController, this);
		this.initialController = initialController;
		this.manageCryptoController = new ManageCryptoController(frameController, this);

		this.adminManager = new AdminManager();
		this.cryptoManager = new CryptoManager(cryptoMarketController);

	}

	public void displayAdminHome() {
		cryptoMarketController.displayCryptoMarketView(true, -1);
	}

	private void adminLogout() {
		initialController.startProgram();
	}

	@Override
	public void setAction(ButtonEnumeration action) {
		switch (action) {
			case SETTINGS -> settingController.displaySettings(true, 0);
			case HOME -> cryptoMarketController.displayCryptoMarketView(true, 0);
			case PORTFOLIO -> System.out.println("PORTFOLIO");
			case BACK -> System.out.println("Back");
			case LOGOUT -> adminLogout();
			case ACCOUNT -> System.out.println("Account");
			case DELETE -> System.out.println("Delete");
			case MANAGE_CRYPTO -> System.out.println("Manage Crypto");
		}
	}
}