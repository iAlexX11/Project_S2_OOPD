package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CryptoMarketView extends Pages {

	private boolean isAdmin;
	private ActionListener currentListener;
	DynamicTable cryptoTable;

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

	public void updateCryptoTable(String name, double currentPrice, double change, double percentage) {
		cryptoTable.update(name, currentPrice, change, percentage);
	}
}