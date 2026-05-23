package org.cryptoBros.presentation.Controllers;

import com.google.gson.*;
import org.cryptoBros.business.AccountManager;
import org.cryptoBros.business.AdminManager;
import org.cryptoBros.business.Crypto;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.business.Liseners.CryptoListener;
import org.cryptoBros.persistence.Exceptions.*;
import org.cryptoBros.presentation.Views.DisplayMessage;

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

	public void deleteCrypto(String cryptoName) {
		try {
			cryptoManager.deleteCrypto(cryptoName);
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
            Gson gson = new Gson();
            JsonElement root = JsonParser.parseReader(reader);

            if (root.isJsonObject()) {
                JsonArray array = new JsonArray();
                array.add(root);
                root = array;
            }

            Crypto[] cryptos = gson.fromJson(root, Crypto[].class);

            for (Crypto crypto : cryptos) {
                cryptoManager.createCrypto(crypto);
            }
        } catch (JsonSyntaxException | NumberFormatException e) {
            DisplayMessage.showMessage(frameController.getMainFrame(), "Invalid JSON format: ");
		}
        catch (IOException e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), "Failed to read file: " + e.getMessage());
		} catch (DbConnectionException | BotGenerationException | CryptoNotAddedException e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
		}
	}

	public void changeCryptoName(String cryptoName, String newName) {
		try {
			cryptoManager.changeCryptoName(cryptoName, newName);
		} catch (CryptoNameAlreadyExists | ErrorChangingCryptoName e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
		}
	}
}