package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Views.ManageCryptoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ManageCryptoController implements ActionListener, BalanceListener {

    private final FrameController frameController;
    private final ManageCryptoView manageCryptoView;

    public ManageCryptoController(FrameController frameController) {
        this.frameController = frameController;
        this.manageCryptoView = new ManageCryptoView();

        manageCryptoView.setActions(this);
    }

    public void displayManageCryptoView(double balance) {
        frameController.displayContent(manageCryptoView);
        manageCryptoView.updateBalance(balance);
        refreshCryptoData();
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
        String command = e.getActionCommand();

        // TODO: Addition of a new cryptocurrency
		if (ButtonEnumeration.ADD_CRYPTO.name().equals(command)) {
            handleAddCrypto();
            return;
        }
    }

    @Override
    public void balanceChanged(double balance) {
        manageCryptoView.updateBalance(balance);
    }
}
