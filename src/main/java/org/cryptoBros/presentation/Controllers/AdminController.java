package org.cryptoBros.presentation.Controllers;

import com.google.gson.*;
import org.cryptoBros.business.Crypto;
import org.cryptoBros.business.CryptoManager;
import org.cryptoBros.persistence.Exceptions.*;
import org.cryptoBros.presentation.Views.DisplayMessage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Handles admin operations such as crypto management and logout. */
public class AdminController {

	private final InitialController initialController;
	private final FrameController frameController;
	private final CryptoManager cryptoManager;

	/**
	 * Creates a new AdminController with the given dependencies.
	 *
	 * @param initialController the controller for the initial screen
	 * @param frameController   the controller managing the main frame
	 * @param cryptoManager     the manager for cryptocurrency operations
	 */
	public AdminController(InitialController initialController, FrameController frameController, CryptoManager cryptoManager) {
		this.frameController = frameController;
		this.initialController = initialController;
		this.cryptoManager = cryptoManager;
	}

	/** Logs out the admin and returns to the initial screen. */
	public void adminLogout() {
		initialController.startProgram();
	}

	/**
	 * Deletes a cryptocurrency by name.
	 *
	 * @param cryptoName the name of the cryptocurrency to delete
	 */
	public void deleteCrypto(String cryptoName) {
		try {
			cryptoManager.deleteCrypto(cryptoName);
		} catch (CryptoNotFoundException e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
		} catch (DbConnectionException e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), "An error occurred while deleting the cryptocurrency.");
		}
	}

	/**
	 * Retrieves all available cryptocurrencies.
	 *
	 * @return a list of all cryptocurrencies, or an empty list if none are found
	 */
	public List<Crypto> getAllCryptos() {
		try {
			return cryptoManager.getAllCryptoList();
		} catch (CryptoNotFoundException | DbConnectionException e) {
			return new ArrayList<>();
		}
	}

	/**
	 * Imports cryptocurrencies from a JSON file.
	 *
	 * @param jsonFile the JSON file containing cryptocurrency data
	 */
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

	/**
	 * Renames a cryptocurrency.
	 *
	 * @param cryptoName the current name of the cryptocurrency
	 * @param newName    the new name for the cryptocurrency
	 */
	public void changeCryptoName(String cryptoName, String newName) {
		try {
			cryptoManager.changeCryptoName(cryptoName, newName);
		} catch (CryptoNameAlreadyExists | ErrorChangingCryptoName e) {
			DisplayMessage.showMessage(frameController.getMainFrame(), e.getMessage());
		}
	}
}