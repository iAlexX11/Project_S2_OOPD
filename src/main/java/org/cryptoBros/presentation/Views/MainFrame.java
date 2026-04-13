package org.cryptoBros.presentation.Views;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        configureWindow();
    }

    private void configureWindow() {
        setTitle("Crypto Bro");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void displayContent(BaseView view) {
        getContentPane().removeAll();
        getContentPane().add(view);
        revalidate();
        repaint();
    }

    public void start() { setVisible(true); }
}
