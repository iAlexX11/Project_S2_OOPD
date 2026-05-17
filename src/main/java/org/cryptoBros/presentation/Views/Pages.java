package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Pages extends BaseView{

	private double balance;
	private JLabel jLBalance;
    private JButton jBHome;
    private JButton jBSettings;
    private JButton jBPortfolio;

    public JPanel setHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(83, 136, 252));
        header.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 0));
        header.setOpaque(true);

        JPanel col1 = new JPanel();
        col1.setLayout(new BoxLayout(col1, BoxLayout.X_AXIS));
        col1.setOpaque(false);

        jBHome = createIconButton("images/home.png");
        jBSettings = createIconButton("images/settings.png");
        jBPortfolio = createIconButton("images/portfolio.png");

        col1.add(Box.createHorizontalGlue());
        col1.add(jBHome);
        col1.add(Box.createHorizontalStrut(20));
        col1.add(jBSettings);
        col1.add(Box.createHorizontalStrut(20));
        col1.add(jBPortfolio);
        col1.add(Box.createHorizontalGlue());

        JPanel col1Wrapper = new JPanel(new BorderLayout());
        col1Wrapper.setOpaque(false);
        col1Wrapper.add(col1, BorderLayout.CENTER);

        JPanel col2 = new JPanel(new GridLayout(2, 1, 0, 5));
        col2.setOpaque(false);
        col2.add(setBalance());
        col2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));

        header.add(col1Wrapper, BorderLayout.WEST);
        header.add(col2, BorderLayout.EAST);

        return header;
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

    public void addHeaderActions(ActionListener listener) {
        jBSettings.setActionCommand(ButtonEnumeration.SETTINGS.name());
        jBPortfolio.setActionCommand(ButtonEnumeration.PORTFOLIO.name());
        jBHome.setActionCommand(ButtonEnumeration.HOME.name());
        jBSettings.addActionListener(listener);
        jBPortfolio.addActionListener(listener);
        jBHome.addActionListener(listener);
    }

	public JLabel setBalance() {
		jLBalance = new JLabel();
		jLBalance.setForeground(Color.WHITE);
		jLBalance.setFont(new Font("Apple Casual", Font.PLAIN, 18));
		return jLBalance;
	}

	public void updateBalance(double balance){
		this.balance = balance;
		jLBalance.setText("The balance is: " + String.format("%.2f", balance) + "€");
		jLBalance.setForeground(Color.WHITE);
		jLBalance.setFont(new Font("Apple Casual", Font.PLAIN, 18));
		jLBalance.revalidate();
		jLBalance.repaint();
	}

}
