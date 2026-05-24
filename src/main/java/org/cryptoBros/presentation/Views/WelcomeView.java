package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.JImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Displays the welcome screen with sign-up and login options.
 */
public class WelcomeView extends BaseView {

    /** Creates a new WelcomeView. */
    public WelcomeView() {}

    /** The sign-up button. */
    private JButton jBSignUp;
    /** The login button. */
    private JButton jBLogin;

    /**
     * Configures the welcome view layout with the logo image, sign-up button, and login button centered on screen.
     */
    @Override
    protected void configureView() {
        JPanel boxLayout = new JPanel();
        boxLayout.setLayout(new BoxLayout(boxLayout, BoxLayout.Y_AXIS));
        boxLayout.setBackground(new Color(239, 247, 255));

        JPanel jPImage= imagePanel();

        jBSignUp = setJBSignUp();
        jBLogin = setJBLogin();

        boxLayout.add(jPImage);
        boxLayout.add(Box.createVerticalStrut(15));
        boxLayout.add(jBSignUp);
        boxLayout.add(Box.createVerticalStrut(15));
        boxLayout.add(jBLogin);
        boxLayout.add(Box.createVerticalStrut(15));

        JPanel centered = new JPanel(new GridBagLayout());
        centered.setBackground(new Color(239, 247, 255));
        centered.add(boxLayout);

        getContent().add(centered, BorderLayout.CENTER);
    }

    /**
     * Registers action listeners for the sign-up and login buttons.
     *
     * @param listener the action listener to attach
     */
    @Override
    public void setActions(ActionListener listener) {
        jBSignUp.setActionCommand(ButtonEnumeration.SIGNUP.name());
        jBLogin.setActionCommand(ButtonEnumeration.LOGIN.name());
        jBSignUp.addActionListener(listener);
        jBLogin.addActionListener(listener);
    }

    private JButton setJBSignUp() {
        JButton jButton = new JButton("Sign Up");
        jButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        jButton.setBackground(new Color(100, 149, 237));
        jButton.setForeground(Color.WHITE);
        jButton.setFont(new Font("Arial", Font.PLAIN, 25));
        jButton.setOpaque(true);
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.setBorderPainted(false);
        jButton.setFocusPainted(false);
        jButton.setPreferredSize(new Dimension(200, 45));
        jButton.setMaximumSize(new Dimension(200, 45));

        return jButton;
    }

    private JButton setJBLogin() {
        JButton jButton = new JButton("Login");
        jButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        jButton.setBackground(Color.WHITE);
        jButton.setForeground(new Color(83, 136, 252));
        jButton.setFont(new Font("Arial", Font.PLAIN, 25));
        jButton.setBorder(BorderFactory.createLineBorder(new Color(83, 136, 252), 2));
        jButton.setOpaque(true);
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.setFocusPainted(false);
        jButton.setPreferredSize(new Dimension(200, 45));
        jButton.setMaximumSize(new Dimension(200, 45));

        return jButton;
    }

    private JPanel imagePanel () {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(550, 400));
        panel.setPreferredSize(new Dimension(550, 400));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JImagePanel jILogo = new JImagePanel("images/logoWelcome.png");
        panel.add(jILogo);

        return panel;
    }
}
