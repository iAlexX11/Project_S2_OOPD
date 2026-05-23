package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Pages extends BaseView{

	private JLabel jLBalance;
    private JLabel jLEstimatedProfit;
	private JButton jBHome;
	private JButton jBSettings;
	private JButton jBPortfolio;
	private JButton jBManageCrypto;
	private boolean isAdmin = false;

	public void setTypeUser(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public JPanel setHeader() {
		return buildHeader(isAdmin ? buildAdminButtons() : buildButtons());
	}

	public void addHeaderActions(ActionListener listener) {
		bindButton(jBHome, ButtonEnumeration.HOME, listener);
		bindButton(jBSettings, ButtonEnumeration.SETTINGS, listener);
		if (!isAdmin) bindButton(jBPortfolio, ButtonEnumeration.PORTFOLIO, listener);
		if (isAdmin) bindButton(jBManageCrypto, ButtonEnumeration.MANAGE_CRYPTO, listener);
	}

	public void init() {
		configureView();
	}

	private JPanel buildHeader(JPanel navWrapper) {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(83, 136, 252));
		header.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 0));
		header.setOpaque(true);
		header.add(navWrapper, BorderLayout.WEST);
		header.add(buildBalancePanel(), BorderLayout.EAST);
		return header;
	}

	private JPanel buildButtons() {
		jBHome = createIconButton("images/home.png");
		jBSettings = createIconButton("images/settings.png");
		jBPortfolio = createIconButton("images/portfolio.png");

		JPanel col1 = new JPanel();
		col1.setLayout(new BoxLayout(col1, BoxLayout.X_AXIS));
		col1.setOpaque(false);
		col1.add(Box.createHorizontalGlue());
		col1.add(jBHome);
		col1.add(Box.createHorizontalStrut(20));
		col1.add(jBSettings);
		col1.add(Box.createHorizontalStrut(20));
		col1.add(jBPortfolio);
		col1.add(Box.createHorizontalGlue());

		return wrapPanel(col1);
	}

	private JPanel buildAdminButtons() {
		jBHome = createIconButton("images/home.png");
		jBSettings = createIconButton("images/settings.png");
		jBManageCrypto = createIconButton("images/manage_crypto.png");

		JPanel col1 = new JPanel();
		col1.setLayout(new BoxLayout(col1, BoxLayout.X_AXIS));
		col1.setOpaque(false);
		col1.add(Box.createHorizontalGlue());
		col1.add(jBHome);
		col1.add(Box.createHorizontalStrut(20));
		col1.add(jBSettings);
		col1.add(Box.createHorizontalStrut(20));
		col1.add(jBManageCrypto);
		col1.add(Box.createHorizontalGlue());

		return wrapPanel(col1);
	}

	private JPanel wrapPanel(JPanel panel) {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(panel, BorderLayout.CENTER);
		return wrapper;
	}

	private JPanel buildBalancePanel() {
		JPanel col2 = new JPanel(new GridLayout(2, 1, 0, 5));
		col2.setOpaque(false);
		col2.add(setBalance());
        if (!isAdmin) col2.add(setEstimatedProfit());
		col2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
		return col2;
	}

	private JButton createIconButton(String imagePath) {
		JButton jButton = new JButton(new ImageIcon(imagePath));
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

	private void bindButton(JButton button, ButtonEnumeration command, ActionListener listener) {
		button.setActionCommand(command.name());
		button.addActionListener(listener);
	}

	public JLabel setBalance() {
		jLBalance = new JLabel();
		jLBalance.setForeground(Color.WHITE);
		jLBalance.setFont(new Font("Apple Casual", Font.PLAIN, 18));
		return jLBalance;
	}

	public void updateBalance(double balance){
		jLBalance.setText("The balance is: " + String.format("%.2f", balance) + "€");
		jLBalance.setForeground(Color.WHITE);
		jLBalance.setFont(new Font("Apple Casual", Font.PLAIN, 18));
		jLBalance.revalidate();
		jLBalance.repaint();
	}

    public JLabel setEstimatedProfit() {
        jLEstimatedProfit = new JLabel();
        jLEstimatedProfit.setForeground(Color.WHITE);
        jLEstimatedProfit.setFont(new Font("Apple Casual", Font.PLAIN, 18));
        return jLEstimatedProfit;
    }

    public void updateEstimatedProfit(double profit) {
		if (jLEstimatedProfit == null) return;
        String sign = profit >= 0 ? "+" : "";
        jLEstimatedProfit.setText("Estimated Profit: " + sign + String.format("%,.2f", profit) + "€");
        jLEstimatedProfit.setForeground(Color.WHITE);
        jLEstimatedProfit.setFont(new Font("Apple Casual", Font.PLAIN, 18));
        jLEstimatedProfit.revalidate();
        jLEstimatedProfit.repaint();
    }
}
