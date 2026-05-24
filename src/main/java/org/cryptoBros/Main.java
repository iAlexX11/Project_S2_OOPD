package org.cryptoBros;

import org.cryptoBros.presentation.Controllers.FrameController;
import org.cryptoBros.presentation.Controllers.InitialController;

import javax.swing.*;

/**
 * Application entry point for the CryptoBros trading platform.
 */
public class Main {

    /**
     * Creates a new Main instance.
     */
    public Main() {}

    /**
     * Launches the application on the Swing event dispatch thread.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrameController frameController = new FrameController();
            InitialController initialController = new InitialController(frameController);
            initialController.startProgram();
            frameController.startFrame();
        });
    }
}