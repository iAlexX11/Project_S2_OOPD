package org.cryptoBros.presentation.Views;

import javax.swing.*;

public class DisplayMessage {
	public static void showMessage(MainFrame mainFrame, String error){
		JOptionPane.showMessageDialog(mainFrame,error);
	}
}