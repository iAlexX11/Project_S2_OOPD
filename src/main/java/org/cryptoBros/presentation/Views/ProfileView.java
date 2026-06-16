package org.cryptoBros.presentation.Views;

import org.cryptoBros.persistence.Exceptions.ErrorChangingProfilePictureException;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Displays the user profile with options to change username, password, and profile picture.
 */
public class ProfileView extends Pages {

	/** The change username button. */
	private JButton changeUsernameButton;
	/** The change password button. */
	private JButton changePasswordButton;
	/** The profile picture component. */
	private ProfilePicture profilePicture;
	/** The confirmed new username. */
	private String confirmedUsername;
	/** The confirmed new password. */
	private char[] confirmedPassword;
	/** The current action listener. */
	private ActionListener actionListener;
	/** Whether the current user is an admin. */
	private final boolean isAdmin;

	/**
	 * Creates a new ProfileView.
	 *
	 * @param isAdmin true if the current user is an admin
	 */
	public ProfileView(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * Builds the central panel of the profile view.
	 * Includes the title, profile picture, and (for non-admin users)
	 * the buttons to change username and password.
	 *
	 * @return the configured core panel
	 */
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
			DisplayMessage.showMessage(new MainFrame(), e.getMessage());
		}

		if (!isAdmin) {
			core.add(Box.createVerticalStrut(20));
			changeUsernameButton = createButton("Change Username", new Color(70, 105, 210));
			core.add(changeUsernameButton);
			core.add(Box.createVerticalStrut(12));

			changePasswordButton = createButton("Change Password", new Color(70, 105, 210));
			core.add(changePasswordButton);
		}

		core.add(Box.createVerticalGlue());

		return core;
	}

    /**
     * Creates a styled {@code JButton} with the given label and background colour.
     *
     * @param text       the button label
     * @param background the background colour of the button
     * @return the configured {@code JButton}
     */
	private JButton createButton(String text, Color background) {
		return getButton(text, background);
	}

	/**
     * Adds the core panel to the centre of the content pane.
     * Called by the parent class during view initialisation.
     */
	@Override
	protected void configureView() {
		getContent().add(setCore(), BorderLayout.CENTER);
	}

    /**
     * Attaches the given {@code ActionListener} to the view, adds the header,
     * and wires up the change-username and change-password buttons for non-admin users.
     *
     * @param listener the listener that handles button action commands
     */
	@Override
	public void setActions(ActionListener listener) {
		this.actionListener = listener;
		getContent().add(setHeader(), BorderLayout.NORTH);
		addHeaderActions(listener);
		getContent().revalidate();

		if(!isAdmin) {
			changeUsernameButton.setActionCommand(ButtonEnumeration.CHANGE_USERNAME.name());
			changeUsernameButton.addActionListener(e -> showChangeUsername());

			changePasswordButton.setActionCommand(ButtonEnumeration.CHANGE_PASSWORD.name());
			changePasswordButton.addActionListener(e -> showChangePassword());
		}
	}

	/**
	 * Opens a dialog that allows the user to change their username.
	 * If a valid username is entered, it is stored and a CHANGE_USERNAME
	 * action event is fired to the registered listener.
	 */
	private void showChangeUsername() {
		confirmedUsername = showSingleFieldDialog("Change Username", "New Username:");
		if (confirmedUsername != null) {
			actionListener.actionPerformed(new ActionEvent(
					this, ActionEvent.ACTION_PERFORMED,
					ButtonEnumeration.CHANGE_USERNAME.name()));
		}
	}

	/**
	 * Opens a dialog that allows the user to change their password.
	 * The user must enter and confirm the new password.
	 * If both passwords match, the confirmed password is stored and a
	 * CHANGE_PASSWORD action event is fired. Otherwise, an error message
	 * is displayed.
	 */
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

		GridBagConstraints buttonConstraints = createFieldConstraints(0, 3);
		Pages.buildAndShowDialog(dialog, formPanel, submitButton, buttonConstraints);
	}

	/**
	 * Returns the confirmed new username.
	 *
	 * @return the confirmed username
	 */
	public String getUsername() {
		return confirmedUsername;
	}

	/**
	 * Returns the confirmed new password.
	 *
	 * @return the confirmed password
	 */
	public char[] getPassword() {
		return confirmedPassword;
	}
}