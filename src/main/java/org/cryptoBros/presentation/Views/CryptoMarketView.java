package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CryptoMarketView extends BaseView {

	private JButton jBSettings;
	private JButton jBPortfolio;
	private JTextField jTBalance;
	private JTextField jTEstimatedProfit;

	@Override
	protected void configureView() {
		JPanel header = setHeader();
		JPanel core = setCore();
		getContent().add(header, BorderLayout.NORTH);
		getContent().add(core, BorderLayout.CENTER);
	}

	@Override
	public void setActions(ActionListener listener) {
		jBSettings.setActionCommand(ButtonEnumeration.SETTINGS.name());
		jBPortfolio.setActionCommand(ButtonEnumeration.PORTFOLIO.name());
		jBSettings.addActionListener(listener);
		jBPortfolio.addActionListener(listener);
	}

	private JPanel setHeader() {
		JPanel header = new JPanel(new GridLayout(1, 2, 45, 0));
		header.setBackground(new Color(83, 136, 252));
		header.setBorder(BorderFactory.createEmptyBorder(30, 50, 10, 0));
		header.setOpaque(true);

		JPanel col1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		col1.setOpaque(false);

		JPanel col2 = new JPanel(new GridLayout(2, 1, 0, 10));
		col2.setOpaque(false);

		jBSettings = setJBSettingsButton();
		jBPortfolio = setJBPortfolioButton();

		col1.add(jBSettings);
		col1.add(Box.createHorizontalStrut(20));
		col1.add(jBPortfolio);

		col2.add(new JLabel("Balance:"));
		col2.add(new JLabel("Estimated Profit:"));

		header.add(col1);
		header.add(col2);

		return header;
	}

	private JPanel setCore() {
		JPanel core = new JPanel(new BorderLayout());
		core.setBackground(new Color(239, 247, 255));
		core.setOpaque(true);

		JPanel jData = dataPanel();
		core.add(jData, BorderLayout.WEST);

		return core;
	}

	private JPanel dataPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(24, 50, 24, 28));
		panel.setPreferredSize(new Dimension(400, 0));
		panel.setBackground(new Color(239, 247, 255));

		return panel;
	}

	private JButton setJBSettingsButton() {
		ImageIcon icon = new ImageIcon("images/settings.png");

		JButton jButton = new JButton(icon);

		jButton.setBackground(Color.WHITE);
		jButton.setOpaque(true);
		jButton.setContentAreaFilled(true);
		jButton.setBorderPainted(false);
		jButton.setFocusPainted(false);

		jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		jButton.setPreferredSize(new Dimension(90, 35));
		jButton.setMaximumSize(new Dimension(90, 35));

		return jButton;
	}

	private JButton setJBPortfolioButton() {
		ImageIcon icon = new ImageIcon("images/portfolio.png");

		JButton jButton = new JButton(icon);

		jButton.setBackground(Color.WHITE);
		jButton.setOpaque(true);
		jButton.setContentAreaFilled(true);
		jButton.setBorderPainted(false);
		jButton.setFocusPainted(false);

		jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		jButton.setPreferredSize(new Dimension(90, 35));
		jButton.setMaximumSize(new Dimension(90, 35));

		return jButton;
	}

}
