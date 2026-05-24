package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.DisplayMessage;
import org.cryptoBros.presentation.Views.MainFrame;

/**
 * Manages the main application frame and content display.
 */
public class FrameController {

    private MainFrame mainFrame;
	private DisplayMessage displayMessage;

    /**
     * Creates a new FrameController and initializes the main frame.
     */
    public FrameController () {
        this.mainFrame = new MainFrame();
		this.displayMessage = new DisplayMessage();
    }

    /**
     * Makes the main frame visible.
     */
    public void startFrame () {
        this.mainFrame.start();
    }

	/**
	 * Displays an error message dialog.
	 *
	 * @param error the error message to display
	 */
	public void showError(String error){
		displayMessage.showMessage(this.mainFrame ,error);
	}

    /**
     * Replaces the current view in the main frame.
     *
     * @param view the new view to display
     */
    public void displayContent(BaseView view) {
        this.mainFrame.displayContent(view);
    }

    /**
     * Returns the main application frame.
     *
     * @return the main frame
     */
    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
