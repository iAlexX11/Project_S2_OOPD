package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CryptoMarketView extends BaseView {

	private JButton jBSettings;
	private JButton jBPortfolio;
	private JLabel jLBalance;
	private JLabel jLProfit;

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
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(83, 136, 252));
		header.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 0));
		header.setOpaque(true);

		JPanel col1 = new JPanel();
		col1.setLayout(new BoxLayout(col1, BoxLayout.X_AXIS));
		col1.setOpaque(false);

		jBSettings = setJBSettingsButton();
		jBPortfolio = setJBPortfolioButton();

		col1.add(Box.createHorizontalGlue());
		col1.add(jBSettings);
		col1.add(Box.createHorizontalStrut(20));
		col1.add(jBPortfolio);
		col1.add(Box.createHorizontalGlue());

		JPanel col1Wrapper = new JPanel(new BorderLayout());
		col1Wrapper.setOpaque(false);
		col1Wrapper.add(col1, BorderLayout.CENTER);

		JPanel col2 = new JPanel(new GridLayout(2, 1, 0, 5));
		col2.setOpaque(false);

		jLBalance = new JLabel();
		jLBalance.setForeground(Color.WHITE);
		jLBalance.setFont(new Font("Apple Casual", Font.PLAIN, 18));
		col2.add(jLBalance);

		jLProfit = new JLabel("Estimated Profit: 0.00€");
		jLProfit.setForeground(Color.WHITE);
		jLProfit.setFont(new Font("Apple Casual", Font.PLAIN, 18));
		col2.add(jLProfit);

		col2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));

		header.add(col1Wrapper, BorderLayout.WEST);
		header.add(col2, BorderLayout.EAST);

		return header;
	}

	public void setBalance(double balance) {
		jLBalance.setText("The balance is: " + String.format("%.2f", balance) + "€");
		jLBalance.setForeground(Color.WHITE);
		jLBalance.setFont(new Font("Apple Casual", Font.PLAIN, 18));
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
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER,0,10));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 50, 24, 28));
		panel.setPreferredSize(new Dimension(400, 0));
		panel.setBackground(new Color(239, 247, 255));

		JLabel title = new JLabel("Current Cryptocurrencies Market");
		title.setFont(new Font("Apple Casual", Font.BOLD, 35));
		title.setForeground(Color.BLACK);
		panel.add(title);

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
