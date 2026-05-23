package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.AdminManager;
import org.cryptoBros.business.Crypto;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.persistence.Exceptions.*;
import org.cryptoBros.presentation.Views.DisplayMessage;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminController {

	private final InitialController initialController;
	private final FrameController frameController;
	private final CryptoManager cryptoManager;
	private final AccountManager accountManager;
	private final AdminManager adminManager;

	public AdminController(InitialController initialController, FrameController frameController, CryptoManager cryptoManager) {
		this.frameController = frameController;
		this.initialController = initialController;
		this.adminManager = new AdminManager();
		this.cryptoManager = cryptoManager;
		this.accountManager = new AccountManager();
	}

	public void adminLogout() {
		initialController.startProgram();
	}

	public void registerCryptoListener(CryptoListener listener) {
		cryptoManager.addCryptoListener(listener);
	}

	public void deleteCrypto(String symbol) {
		try {
			cryptoManager.deleteCrypto(symbol);
		} catch (CryptoNotFoundException e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
		} catch (DbConnectionException e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), "An error occurred while deleting the cryptocurrency.");
		}
	}

	public List<Crypto> getAllCryptos() {
		try {
			return cryptoManager.getAllCryptoList();
		} catch (CryptoNotFoundException | DbConnectionException e) {
			return new ArrayList<>();
		}
	}

	public void addCryptoFromFile(File jsonFile) {
		try (FileReader reader = new FileReader(jsonFile)) {
			Crypto crypto = new Gson().fromJson(reader, Crypto.class);
			cryptoManager.createCrypto(crypto);
		} catch (IOException e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), "Failed to read file: " + e.getMessage());
		} catch (DbConnectionException | BotGenerationException | CryptoNotAddedException e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
		}
	}

	public void changeCryptoName(String symbol, String newName) {
		try {
			cryptoManager.changeCryptoName(symbol, newName);
		} catch (CryptoNameAlreadyExists | ErrorChangingCryptoName e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
		}
	}
}