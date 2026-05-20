package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.DisplayMessage;
import org.cryptoBros.presentation.Views.MainFrame;

public class FrameController {

    private MainFrame mainFrame;
	private DisplayMessage displayMessage;

    public FrameController () {
        this.mainFrame = new MainFrame();
		this.displayMessage = new DisplayMessage();
    }

    public void startFrame () {
        this.mainFrame.start();
    }

	public void showError(String error){
		displayMessage.showMessage(this.mainFrame ,error);
	}

    public void displayContent(BaseView view) {
        this.mainFrame.displayContent(view);
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
