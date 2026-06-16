package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Displays the sign-up form with username, email, and password fields.
 */
public class SignUpView extends BaseView {

	/** Creates a new SignUpView. */
	public SignUpView() {}

	/** The username input field. */
	private JTextField jTFUsername;
	/** The email input field. */
	private JTextField jTFEmail;
	/** The password input field. */
	private JPasswordField jTFPassword;
	/** The confirmation password input field. */
	private JPasswordField jTFConfirmPassword;
	/** The login navigation button. */
	private JButton jBLogin;
	/** The sign-up confirm button. */
	private JButton jBConfirm;

	/**
	 * Configures the sign-up view layout with a header, input fields for credentials, and a logo image panel.
	 */
	@Override
	public void configureView () {
		getContent().add(buildHeader("Sign Up"), BorderLayout.NORTH);
		getContent().add(buildCore(dataPanel(), buildImagePanel()), BorderLayout.CENTER);
	}

	private JPanel dataPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 28));
		panel.setPreferredSize(new Dimension(400, 0));
		panel.setBackground(new Color(239, 247, 255));

		jTFUsername = buildTextField("Username");
		jTFEmail = buildTextField("example@gmail.com");
		jTFPassword = buildPasswordField();
		jTFConfirmPassword = buildPasswordField();
		jBLogin = buildLinkButton("Already have an account? Login");
		jBConfirm = buildConfirmButton("Sign Up");

		addField(panel,"Username", jTFUsername);
		addField(panel,"Email", jTFEmail);
		addField(panel,"Password", jTFPassword);
		addField(panel,"Confirm Password", jTFConfirmPassword);
		panel.add(jBLogin);
		panel.add(Box.createVerticalStrut(10));
		panel.add(jBConfirm);

		return panel;
	}

	/**
	 * Registers action listeners for the sign-up confirm and login navigation buttons.
	 *
	 * @param listener the action listener to attach
	 */
	@Override
	public void setActions(ActionListener listener) {
		jBConfirm.setActionCommand(ButtonEnumeration.CONFIRM_SIGNUP.name());
		jBLogin.setActionCommand(ButtonEnumeration.LOGIN.name());
		jBConfirm.addActionListener(listener);
		jBLogin.addActionListener(listener);
	}

	/**
	 * Returns the entered password.
	 *
	 * @return the password as a char array
	 */
	public char[] getPassword() {
		return jTFPassword.getPassword();
	}

	/**
	 * Returns the entered confirmation password.
	 *
	 * @return the confirmation password as a char array
	 */
	public char[] getConfirmPassword() {
		return jTFConfirmPassword.getPassword();
	}

	/**
	 * Returns the entered email address.
	 *
	 * @return the email address
	 */
	public String getEmail() {
		return jTFEmail.getText();
	}

	/**
	 * Returns the entered username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return jTFUsername.getText();
	}

}