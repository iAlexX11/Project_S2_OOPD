package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.ListenersPersistence.CryptoSelectedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Displays the cryptocurrency market table with live price data.
 */
public class CryptoMarketView extends Pages {

	/** Whether the current user is an admin. */
	private boolean isAdmin;
	/** The current action listener. */
	private ActionListener currentListener;
	/** The cryptocurrency data table. */
	DynamicTable cryptoTable;

	/** Creates a new CryptoMarketView. */
	public CryptoMarketView() {}

	@Override
	protected void configureView() {
		getContent().add(setCore(), BorderLayout.CENTER);
	}

	@Override
	public void setActions(ActionListener listener) {
		getContent().add(setHeader(), BorderLayout.NORTH);
		addHeaderActions(listener);
		getContent().revalidate();
	}

	private void applyActions() {
		if (currentListener == null) return;
		addHeaderActions(currentListener);
	}

    /**
     * Registers a listener for row click events.
     * @param listener the listener for row click events
     */
    public void setOnRowClick(CryptoSelectedListener listener) {
        cryptoTable.setOnRowClick(listener);
    }

	private JPanel setCore() {
		JPanel core = new JPanel(new BorderLayout());
		core.setBackground(new Color(239, 247, 255));
		core.setOpaque(true);

		JPanel jData = dataPanel();
		core.add(jData, BorderLayout.CENTER);

		return core;
	}

	private JPanel dataPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 50, 24, 28));
		panel.setPreferredSize(new Dimension(400, 0));
		panel.setBackground(new Color(239, 247, 255));

		JLabel title = new JLabel("Current Cryptocurrencies Market");
		title.setFont(new Font("Apple Casual", Font.BOLD, 35));
		title.setForeground(Color.BLACK);
		cryptoTable = new DynamicTable();

		panel.add(title, BorderLayout.NORTH);
		panel.add(cryptoTable, BorderLayout.CENTER);

		return panel;
	}

    /**
     * Updates or adds a cryptocurrency row in the table.
     * @param symbol       the ticker symbol
     * @param name         the display name
     * @param currentPrice the current price
     * @param change       the price change
     * @param percentage   the percentage change
     */
    public void updateCryptoTable(String symbol, String name, double currentPrice, double change, double percentage) {
        cryptoTable.update(symbol, name, currentPrice, change, percentage);
    }

    /** Removes all rows from the crypto table. */
    public void clearCryptoTable() {
        cryptoTable.clearRows();
    }
}
