package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.JImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class WelcomeView extends BaseView {

    private JButton jBSignUp;
    private JButton jBLogin;

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

        getContent().add(boxLayout, BorderLayout.CENTER);
    }

    @Override
    public void setActions(ActionListener listener) {
        jBSignUp.setActionCommand("SIGNUP");
        jBLogin.setActionCommand("LOGIN");
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
