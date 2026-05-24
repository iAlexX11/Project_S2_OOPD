package org.cryptoBros.presentation.Views;

import javax.swing.*;

/**
 * Utility class for displaying message dialogs.
 */
public class DisplayMessage {
	/**
	 * Creates a new DisplayMessage instance.
	 */
	public DisplayMessage() {}

	/**
	 * Shows a message dialog on the given frame.
	 *
	 * @param mainFrame the parent frame
	 * @param error     the message to display
	 */
	public static void showMessage(MainFrame mainFrame, String error){
		JOptionPane.showMessageDialog(mainFrame,error);
	}
}