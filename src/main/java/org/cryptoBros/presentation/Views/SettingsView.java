package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Displays the settings page with logout, account management, and delete options.
 */
public class SettingsView extends Pages{

    /** The back navigation button. */
    private JButton backButton;
    /** The logout button. */
    private JButton logoutButton;
    /** The account management button. */
    private JButton accountManagementButton;
    /** The delete account button. */
    private JButton deleteAccountButton;
	/** Whether the current user is an admin. */
	private boolean isAdmin = false;

	/**
	 * Creates a new SettingsView.
	 *
	 * @param isAdmin true if the current user is an admin
	 */
	public SettingsView(boolean isAdmin) {
		this.isAdmin = isAdmin;
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

		if (!isAdmin) {
			deleteAccountButton = createButton("Delete your account", new Color(220, 40, 40));
			core.add(deleteAccountButton);
		}
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

	/**
	 * Configures the settings view layout with logout, account management, and delete account buttons.
	 */
	@Override
	protected void configureView() {
		getContent().add(setCore(), BorderLayout.CENTER);
	}

	/**
	 * Registers action listeners for the header navigation, logout, account management, and delete account buttons.
	 *
	 * @param listener the action listener to attach
	 */
	@Override
	public void setActions(ActionListener listener) {
		getContent().add(setHeader(), BorderLayout.NORTH);
		addHeaderActions(listener);
		getContent().revalidate();
		logoutButton.setActionCommand(ButtonEnumeration.LOGOUT.name());
		logoutButton.addActionListener(listener);

		accountManagementButton.setActionCommand(ButtonEnumeration.PROFILE.name());
		accountManagementButton.addActionListener(listener);
		if (!isAdmin) {
			deleteAccountButton.setActionCommand(ButtonEnumeration.DELETE.name());
			deleteAccountButton.addActionListener(listener);
		}

	}
}
