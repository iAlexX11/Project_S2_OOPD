package org.cryptoBros.presentation.Controllers;

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
    }

    public BaseView getView() {
        refreshCryptoData();
		return manageCryptoView;
    }

    //TODO refresh crypto table with information from the database
    private void refreshCryptoData() {
        // Replace with real data from your business layer
        Object[][] data = {
                { "Bitcoin",  "", "" },
                { "LSCoin",   "", "" },
                { "Tether",   "", "" },
                { "Ethereum", "", "" },
                { "XRP",      "", "" },
                { "BNB",      "", "" },
                { "Solana",   "", "" },
                { "Dogecoin", "", "" },
                { "Cardano",  "", "" },
                { "TRON",     "", "" },
        };
        manageCryptoView.setCryptoData(data);
    }

    private void handleAddCrypto() {
        JFileChooser fileChooser = manageCryptoView.createFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath()); // wire to business layer
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
