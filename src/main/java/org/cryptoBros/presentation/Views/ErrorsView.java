package org.cryptoBros.presentation.Views;

import javax.swing.*;

public class ErrorsView {
	public static void showError(MainFrame mainFrame, String error){
		JOptionPane.showMessageDialog(mainFrame,error);
	}
}