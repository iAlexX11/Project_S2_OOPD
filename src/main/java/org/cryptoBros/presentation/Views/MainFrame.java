package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;

/**
 * The main application window.
 */
public class MainFrame extends JFrame {

    /**
     * Creates and configures the main application window.
     */
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

    /**
     * Replaces the current content with the given view.
     *
     * @param view the view to display
     */
    public void displayContent(BaseView view) {
        getContentPane().removeAll();
        getContentPane().add(view, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Makes the frame visible.
     */
    public void start() { setVisible(true); }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
