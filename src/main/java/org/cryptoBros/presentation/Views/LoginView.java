package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.JImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Displays the login form with username and password fields.
 */
public class LoginView extends BaseView {

    /** The username input field. */
    private JTextField jTFUsername;
    /** The password input field. */
    private JPasswordField jTFPassword;
    /** The sign-up navigation button. */
    private JButton jBSignUp;
    /** The login confirm button. */
    private JButton jBConfirm;

    /**
     * Creates a new LoginView.
     */
    public LoginView() {}

    /**
     * Configures the login view layout with a header, input fields, and a logo image panel.
     */
    @Override
    public void configureView () {
        JPanel header = setHeader();
        JPanel core = setCore();

        getContent().add(header, BorderLayout.NORTH);
        getContent().add(core, BorderLayout.CENTER);
    }

    private JPanel setCore() {
        JPanel core = new JPanel(new GridBagLayout());
        core.setBackground(new Color(239, 247, 255));
        core.setOpaque(true);

        JPanel jData = dataPanel();
        JPanel jImage = imagePanel();

        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(239, 247, 255));
        row.add(jData,  BorderLayout.WEST);
        row.add(jImage, BorderLayout.EAST);

        core.add(row);

        return core;
    }

    private JPanel dataPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 50, 24, 28));
        panel.setPreferredSize(new Dimension(400, 0));
        panel.setBackground(new Color(239, 247, 255));

        JLabel username = setText("Username/Email");
        jTFUsername = setTextField("Username/email@gmail.com");

        JLabel password = setText("Password");
        jTFPassword = setPassword();

        jBSignUp = setJBSignUp();

        jBConfirm = setJBConfirm();

        panel.add(Box.createVerticalStrut(80));
        panel.add(username);
        panel.add(Box.createVerticalStrut(10));
        panel.add(jTFUsername);
        panel.add(Box.createVerticalStrut(15));
        panel.add(password);
        panel.add(Box.createVerticalStrut(10));
        panel.add(jTFPassword);
        panel.add(Box.createVerticalStrut(15));
        panel.add(jBSignUp);
        panel.add(Box.createVerticalStrut(10));
        panel.add(jBConfirm);

        return panel;
    }

    private JButton setJBSignUp() {
        JButton jButton = new JButton("Don't have an account? Creat one");
        jButton.setContentAreaFilled(false);
        jButton.setBorderPainted(false);
        jButton.setForeground(new Color(83, 136, 252));
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.setFont(new Font("Apple Casual", Font.PLAIN, 14));
        jButton.setHorizontalAlignment(SwingConstants.LEFT);

        return jButton;
    }

    private JButton setJBConfirm() {
        JButton jButton = new JButton("Log In");
        jButton.setBackground(new Color(100, 149, 237));
        jButton.setForeground(Color.WHITE);
        jButton.setFont(new Font("Arial", Font.PLAIN, 20));
        jButton.setOpaque(true);
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.setBorderPainted(false);
        jButton.setFocusPainted(false);
        jButton.setPreferredSize(new Dimension(150, 35));
        jButton.setMaximumSize(new Dimension(150, 35));

        return jButton;
    }

    private JPasswordField setPassword() {
        JPasswordField password = new JPasswordField();
        password.setPreferredSize(new Dimension(500, 25));
        password.setMaximumSize(new Dimension(500, 25));
        password.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220)));
        password.setForeground(Color.GRAY);

        return password;
    }

    private JTextField setTextField(String placeholder) {
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(500, 25));
        jTextField.setMaximumSize(new Dimension(500, 25));
        jTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220)));
        jTextField.setText(placeholder);
        jTextField.setForeground(Color.GRAY);
        jTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (jTextField.getText().equals(placeholder)) {
                    jTextField.setText("");
                    jTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (jTextField.getText().isEmpty()) {
                    jTextField.setForeground(Color.GRAY);
                    jTextField.setText(placeholder);
                }
            }
        });

        return jTextField;
    }

    private JLabel setText (String text) {
        JLabel jLText = new JLabel(text);
        jLText.setFont(new Font("Apple Casual", Font.PLAIN, 20));

        return jLText;
    }

    private JPanel imagePanel () {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));
        panel.setMaximumSize(new Dimension(550, 350));
        panel.setPreferredSize(new Dimension(500, 420));
        panel.setBackground(new Color(239, 247, 255));

        JImagePanel jILogo = new JImagePanel("images/logo.png");
        panel.add(jILogo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel setHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(83, 136, 252));
        header.setBorder(BorderFactory.createEmptyBorder(50, 50, 10, 0));
        header.setOpaque(true);

        JLabel title = new JLabel("Log In");
        title.setFont(new Font("Apple Casual", Font.BOLD, 50));
        title.setForeground(Color.WHITE);

        header.add(title, BorderLayout.SOUTH);
        return header;
    }

    /**
     * Registers action listeners for the login confirm and sign-up navigation buttons.
     *
     * @param listener the action listener to attach
     */
    @Override
    public void setActions(ActionListener listener) {
        jBConfirm.setActionCommand(ButtonEnumeration.CONFIRM_LOGIN.name());
        jBSignUp.setActionCommand(ButtonEnumeration.SIGNUP.name());
        jBConfirm.addActionListener(listener);
        jBSignUp.addActionListener(listener);
    }

	/**
	 * Returns the entered password.
	 *
	 * @return the password as a character array
	 */
	public char[] getPassword() {
		return jTFPassword.getPassword();
	}

	/**
	 * Returns the entered username or email.
	 *
	 * @return the username or email text
	 */
	public String getUsername() {
		return jTFUsername.getText();
	}
}
