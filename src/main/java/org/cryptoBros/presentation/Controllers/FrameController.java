package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.ErrorsView;
import org.cryptoBros.presentation.Views.MainFrame;

public class FrameController {

    private MainFrame mainFrame;
	private ErrorsView errorsView;

    public FrameController () {
        this.mainFrame = new MainFrame();
		this.errorsView = new ErrorsView();
    }

    public void startFrame () {
        this.mainFrame.start();
    }

	public void showError(String error){
		errorsView.showError(this.mainFrame ,error);
	}

    public void displayContent(BaseView view) {
        this.mainFrame.displayContent(view);
    }

    public MainFrame getMainFram() {
        return mainFrame;
    }
}
