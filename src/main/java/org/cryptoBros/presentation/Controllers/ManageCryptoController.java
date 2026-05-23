package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Crypto;
import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.ManageCryptoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class ManageCryptoController implements ActionListener, BalanceListener {

    private final FrameController frameController;
    private final ManageCryptoView manageCryptoView;
	private final Navigation navigation;
	private final AdminController adminController;

    public ManageCryptoController(FrameController frameController, Navigation navigation, AdminController adminController, boolean isAdmin) {
        this.frameController = frameController;
        this.manageCryptoView = new ManageCryptoView();
		this.navigation = navigation;
		this.adminController = adminController;
		manageCryptoView.setTypeUser(isAdmin);
        manageCryptoView.setActions(this);
        manageCryptoView.setDeleteCallback(this::handleDeleteCrypto);
		manageCryptoView.setEditCallback(this::editCrypto);
    }

	private void editCrypto(int row) {
		String symbol = manageCryptoView.getCryptoSymbolAtRow(row);
		String newName = manageCryptoView.getCryptoName();

		if (newName == null || newName.isBlank()) return;

		adminController.changeCryptoName(symbol, newName);
		refreshCryptoData();
	}

	private void refreshCryptoData() {
		List<Crypto> cryptos = adminController.getAllCryptos();
		Object[][] data = new Object[cryptos.size()][3];
		for (int i = 0; i < cryptos.size(); i++) {
			data[i] = new Object[]{ cryptos.get(i).getName(), "", "" };
		}
		manageCryptoView.setCryptoData(data);
	}

	public BaseView getView() {
        refreshCryptoData();
		return manageCryptoView;
    }

    private void handleDeleteCrypto(int row) {
        String symbol = manageCryptoView.getCryptoSymbolAtRow(row);
        int confirm = JOptionPane.showConfirmDialog(null,
                "Delete " + symbol + "? All holders will be refunded automatically.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            adminController.deleteCrypto(symbol);
            refreshCryptoData();
        }
    }

    private void handleAddCrypto() {
        JFileChooser fileChooser = manageCryptoView.createFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            adminController.addCryptoFromFile(selectedFile);
            refreshCryptoData();
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
		String command = e.getActionCommand();
		if (ButtonEnumeration.ADD_CRYPTO.name().equals(command)) {
			handleAddCrypto();
			return;
		}
		switch (buttonEnumeration) {
			case SETTINGS -> navigation.navigate(PagesName.SETTING);
			case HOME -> navigation.navigate(PagesName.CRYPTO_MARKET);
			case PORTFOLIO -> navigation.navigate(PagesName.PORTFOLIO);
			case LOGOUT -> adminController.adminLogout();
		}
	}

    @Override
    public void balanceChanged(double balance) {
        manageCryptoView.updateBalance(balance);
    }
}
