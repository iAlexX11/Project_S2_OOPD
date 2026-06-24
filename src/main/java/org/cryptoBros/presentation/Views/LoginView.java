package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends BaseView {

	private JTextField jTFUsername;
	private JPasswordField jTFPassword;
	private JButton jBSignUp;
	private JButton jBConfirm;

	public LoginView() {}

	@Override
	public void configureView() {
		getContent().add(buildHeader("Log In"), BorderLayout.NORTH);
		getContent().add(buildCore(dataPanel(), buildImagePanel()), BorderLayout.CENTER);
	}

	private JPanel dataPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(24, 50, 24, 28));
		panel.setPreferredSize(new Dimension(400, 0));
		panel.setBackground(new Color(239, 247, 255));

		jTFUsername = buildTextField("Username/email@gmail.com");
		jTFPassword = buildPasswordField();
		jBSignUp = buildLinkButton("Don't have an account? Create one");
		jBConfirm = buildConfirmButton("Log In");

		panel.add(Box.createVerticalStrut(80));
		addField(panel,"Username/Email", jTFUsername);
		addField(panel,"Password", jTFPassword);
		panel.add(jBSignUp);
		panel.add(Box.createVerticalStrut(10));
		panel.add(jBConfirm);

		return panel;
	}

	@Override
	public void setActions(ActionListener listener) {
		jBConfirm.setActionCommand(ButtonEnumeration.CONFIRM_LOGIN.name());
		jBSignUp.setActionCommand(ButtonEnumeration.SIGNUP.name());
		jBConfirm.addActionListener(listener);
		jBSignUp.addActionListener(listener);
	}

	public char[] getPassword() { return jTFPassword.getPassword(); }
	public String getUsername() { return jTFUsername.getText(); }
}