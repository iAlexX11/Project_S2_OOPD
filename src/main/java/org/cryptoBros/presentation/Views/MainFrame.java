package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        configureWindow();
    }

    private void configureWindow() {
        setTitle("Crypto Bro");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    public void displayContent(BaseView view) {
        getContentPane().removeAll();
        getContentPane().add(view, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void start() { setVisible(true); }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
