package org.cryptoBros.presentation.Views;

import org.cryptoBros.persistence.Exceptions.ErrorChangingProfilePictureException;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AccountView extends Pages {

	private JButton backButton;
	private JButton changeUsernameButton;
	private JButton changePasswordButton;
	private JButton changePicButton;
	private ProfilePicture profilePicture;

	private JPanel setCore() {
		JPanel core = new JPanel();
		core.setLayout(new BoxLayout(core, BoxLayout.Y_AXIS));
		core.setBackground(new Color(239, 247, 255));

		// Title
		JLabel titleLabel = new JLabel("Profile Settings", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		core.add(Box.createVerticalGlue());
		core.add(titleLabel);
		core.add(Box.createVerticalStrut(20));

		try {
			profilePicture = new ProfilePicture("images/profile_pic.jpg");
			profilePicture.setPreferredSize(new Dimension(100, 100));
			profilePicture.setMaximumSize(new Dimension(100, 100));
			profilePicture.setAlignmentX(Component.CENTER_ALIGNMENT);
			core.add(profilePicture);
		} catch (ErrorChangingProfilePictureException e) {
			ErrorsView.showError(new MainFrame(), e.getMessage());
		}

		core.add(Box.createVerticalStrut(20));
		changePicButton = createButton("Change Profile Picture", new Color(70, 105, 210));
		core.add(changePicButton);
		core.add(Box.createVerticalStrut(12));

		changeUsernameButton = createButton("Change Username", new Color(70, 105, 210));
		core.add(changeUsernameButton);
		core.add(Box.createVerticalStrut(12));

		changePasswordButton = createButton("Change Password", new Color(70, 105, 210));
		core.add(changePasswordButton);

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
	protected void configureView() {
		getContent().add(setCore(), BorderLayout.CENTER);
	}

	@Override
	public void setActions(ActionListener listener) {
		getContent().add(setHeader(), BorderLayout.NORTH);
		addHeaderActions(listener);
		getContent().revalidate();
		changePicButton.setActionCommand(ButtonEnumeration.CHANGE_PROFILE_PIC.name());
		changePicButton.addActionListener(listener);

		changeUsernameButton.setActionCommand(ButtonEnumeration.CHANGE_USERNAME.name());
		changeUsernameButton.addActionListener(listener);

		changePasswordButton.setActionCommand(ButtonEnumeration.CHANGE_PASSWORD.name());
		changePasswordButton.addActionListener(listener);
	}
	private JDialog createBaseDialog(String title) {
		JDialog dialog = new JDialog();
		dialog.setTitle(title);
		dialog.setModal(true);
		dialog.setLocationRelativeTo(getContent());
		dialog.setLocationRelativeTo(null);
		return dialog;
	}

	private GridBagConstraints createGbc(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.insets = new Insets(8, 10, 8, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = x == 1 ? 1.0 : 0;
		return gbc;
	}

	public void showChangeUsername() {
		JDialog dialog = createBaseDialog("Change Username");
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTextField newUsername = new JTextField(15);
		panel.add(new JLabel("New Username:"), createGbc(0, 0));
		panel.add(newUsername, createGbc(1, 0));

		JButton confirmBtn = new JButton("Confirm");
		GridBagConstraints gbc = createGbc(0, 1);
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(confirmBtn, gbc);

		dialog.add(panel);
		dialog.pack();
		dialog.setVisible(true);
	}

	public void showChangePassword() {
		JDialog dialog = createBaseDialog("Change Password");
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPasswordField newPassword = new JPasswordField(15);
		JPasswordField confirmPassword = new JPasswordField(15);

		panel.add(new JLabel("New Password:"), createGbc(0, 0));
		panel.add(newPassword, createGbc(1, 0));
		panel.add(new JLabel("Confirm Password:"), createGbc(0, 1));
		panel.add(confirmPassword, createGbc(1, 1));

		JButton confirmBtn = new JButton("Confirm");
		GridBagConstraints gbc = createGbc(0, 2);
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(confirmBtn, gbc);

		dialog.add(panel);
		dialog.pack();
		dialog.setVisible(true);
	}

	public void showChangeProfilePicture() {
		//TODO: ASK FOR A NEW PICTURE
	}
}
