package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SettingsView extends Pages{

    private JButton backButton;
    private JButton logoutButton;
    private JButton accountManagementButton;
    private JButton deleteAccountButton;

    @Override
    public void configureView () {
        JPanel header = setHeader();
        JPanel core = setCore();

        getContent().add(header, BorderLayout.NORTH);
        getContent().add(core, BorderLayout.CENTER);
    }

	@Override
    public JPanel setHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(83, 136, 252));
        header.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 0));
        header.setOpaque(true);


        backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(true);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(70, 45));

        JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.Y_AXIS));
        balancePanel.setBackground(new Color(70, 105, 210));
        balancePanel.setOpaque(true);
        balancePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

		JLabel jLBalance = setBalance();

        header.add(backButton, BorderLayout.WEST);
        header.add(jLBalance, BorderLayout.EAST);

        return header;
    }

    private JPanel setCore() {
        JPanel core = new JPanel();
        core.setLayout(new BoxLayout(core, BoxLayout.Y_AXIS));
        core.setBackground(new Color(239, 247, 255));

        // Title
        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        core.add(Box.createVerticalGlue());
        core.add(titleLabel);
        core.add(Box.createVerticalStrut(30));

        logoutButton = createButton("Logout", new Color(70, 105, 210));
        core.add(logoutButton);
        core.add(Box.createVerticalStrut(12));

        accountManagementButton = createButton("Account Management", new Color(70, 105, 210));
        core.add(accountManagementButton);
        core.add(Box.createVerticalStrut(12));

        deleteAccountButton = createButton("Delete your account", new Color(220, 40, 40));
        core.add(deleteAccountButton);

        core.add(Box.createVerticalGlue());

        return core;
    }

    private JButton createButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(300, 50));
        button.setMaximumSize(new Dimension(300, 50));
        return button;
    }

    @Override
    public void setActions(ActionListener listener) {
        backButton.setActionCommand(ButtonEnumeration.BACK.name());
        backButton.addActionListener(listener);

        logoutButton.setActionCommand(ButtonEnumeration.LOGOUT.name());
        logoutButton.addActionListener(listener);

        accountManagementButton.setActionCommand(ButtonEnumeration.ACCOUNT.name());
        accountManagementButton.addActionListener(listener);

        deleteAccountButton.setActionCommand(ButtonEnumeration.DELETE.name());
        deleteAccountButton.addActionListener(listener);
    }
}
