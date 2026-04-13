package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.MainFrame;

public class FrameController {

    private MainFrame mainFrame;

    public FrameController () {
        this.mainFrame = new MainFrame();
    }

    public void startFrame () {
        this.mainFrame.start();
    }

    public void displayContent(BaseView view) {
        this.mainFrame.displayContent(view);
    }
}
