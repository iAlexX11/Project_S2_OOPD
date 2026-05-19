package org.cryptoBros.presentation.Views;

import org.cryptoBros.persistence.Exceptions.ErrorChangingProfilePictureException;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ProfileView extends Pages {

	private JButton changeUsernameButton;
	private JButton changePasswordButton;
	private JButton changePicButton;
	private ProfilePicture profilePicture;
	private String confirmedUsername;
	private char[] confirmedPassword;
	private ActionListener actionListener;

	private JPanel setCore() {
		JPanel core = new JPanel();
		core.setLayout(new BoxLayout(core, BoxLayout.Y_AXIS));
		core.setBackground(new Color(239, 247, 255));

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
		this.actionListener = listener;
		getContent().add(setHeader(), BorderLayout.NORTH);
		addHeaderActions(listener);
		getContent().revalidate();

		changePicButton.setActionCommand(ButtonEnumeration.CHANGE_PROFILE_PIC.name());
		changePicButton.addActionListener(listener);

		changeUsernameButton.setActionCommand(ButtonEnumeration.CHANGE_USERNAME.name());
		changeUsernameButton.addActionListener(e -> showChangeUsername());

		changePasswordButton.setActionCommand(ButtonEnumeration.CHANGE_PASSWORD.name());
		changePasswordButton.addActionListener(e -> showChangePassword());
	}

	private JDialog createBaseDialog(String title) {
		JDialog dialog = new JDialog();
		dialog.setTitle(title);
		dialog.setModal(true);
		return dialog;
	}

	private GridBagConstraints createFieldConstraints(int column, int row) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.insets = new Insets(8, 10, 8, 10);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = column == 1 ? 1.0 : 0;
		return constraints;
	}

	private void showChangeUsername() {
		JDialog dialog = createBaseDialog("Change Username");
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTextField usernameField = new JTextField(15);
		formPanel.add(new JLabel("New Username:"), createFieldConstraints(0, 0));
		formPanel.add(usernameField, createFieldConstraints(1, 0));

		JButton submitButton = new JButton("Confirm");
		submitButton.addActionListener(e -> {
			confirmedUsername = usernameField.getText().trim();
			actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ButtonEnumeration.CHANGE_USERNAME.name()));
			dialog.dispose();
		});

		GridBagConstraints buttonConstraints = createFieldConstraints(0, 1);
		buttonConstraints.gridwidth = 2;
		buttonConstraints.fill = GridBagConstraints.NONE;
		buttonConstraints.anchor = GridBagConstraints.CENTER;
		formPanel.add(submitButton, buttonConstraints);

		dialog.add(formPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private void showChangePassword() {
		JDialog dialog = createBaseDialog("Change Password");
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPasswordField newPasswordField = new JPasswordField(15);
		JPasswordField confirmPasswordField = new JPasswordField(15);

		JLabel errorLabel = new JLabel("Passwords do not match");
		errorLabel.setForeground(Color.RED);
		errorLabel.setVisible(false);

		formPanel.add(new JLabel("New Password:"), createFieldConstraints(0, 0));
		formPanel.add(newPasswordField, createFieldConstraints(1, 0));
		formPanel.add(new JLabel("Confirm Password:"), createFieldConstraints(0, 1));
		formPanel.add(confirmPasswordField, createFieldConstraints(1, 1));

		GridBagConstraints errorConstraints = createFieldConstraints(0, 2);
		errorConstraints.gridwidth = 2;
		errorConstraints.anchor = GridBagConstraints.CENTER;
		formPanel.add(errorLabel, errorConstraints);

		JButton submitButton = new JButton("Confirm");
		submitButton.addActionListener(e -> {
			if (Arrays.equals(newPasswordField.getPassword(), confirmPasswordField.getPassword())) {
				confirmedPassword = newPasswordField.getPassword();
				actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ButtonEnumeration.CHANGE_PASSWORD.name()));
				dialog.dispose();
			} else {
				errorLabel.setVisible(true);
				dialog.pack();
			}
		});

		GridBagConstraints buttonConstraints = createFieldConstraints(0, 3); // fila 3
		buttonConstraints.gridwidth = 2;
		buttonConstraints.fill = GridBagConstraints.NONE;
		buttonConstraints.anchor = GridBagConstraints.CENTER;
		formPanel.add(submitButton, buttonConstraints);

		dialog.add(formPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	public String getUsername() {
		return confirmedUsername;
	}

	public char[] getPassword() {
		return confirmedPassword;
	}

	public void showChangeProfilePicture() {
		//TODO: ASK FOR A NEW PICTURE
	}
}