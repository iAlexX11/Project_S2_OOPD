package org.cryptoBros;

import org.cryptoBros.presentation.Controllers.InitialController;
import org.cryptoBros.presentation.Views.MainFrame;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            InitialController initialController = new InitialController(mainFrame);
            initialController.startProgram();
            mainFrame.start();
        });
    }
}