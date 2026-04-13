package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.JImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LoginView extends BaseView {

    private JTextField jTFUsername;
    private JPasswordField jTFPassword;
    private JButton jBSignUp;
    private JButton jBConfirm;

    @Override
    public void configureView () {
        JPanel header = setHeader();
        JPanel core = setCore();

        getContent().add(header, BorderLayout.NORTH);
        getContent().add(core, BorderLayout.CENTER);
    }

    private JPanel setCore() {
        JPanel core = new JPanel(new BorderLayout());
        core.setBackground(new Color(239, 247, 255));
        core.setOpaque(true);

        JPanel jData = dataPanel();
        JPanel jImage = imagePanel();

        core.add(jData, BorderLayout.WEST);
        core.add(jImage, BorderLayout.EAST);

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
        panel.setPreferredSize(new Dimension(550, 350));
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

    @Override
    public void setActions(ActionListener listener) {
        jBConfirm.setActionCommand(ButtonEnumeration.CONFIRM_LOGIN.name());
        jBSignUp.setActionCommand(ButtonEnumeration.SIGNUP.name());
        jBConfirm.addActionListener(listener);
        jBSignUp.addActionListener(listener);
    }
}
