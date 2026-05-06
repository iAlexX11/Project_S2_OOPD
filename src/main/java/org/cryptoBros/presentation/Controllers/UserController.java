package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.AccountManager;
import org.cryptoBros.presentation.ButtonEnumeration;
import org.cryptoBros.presentation.ListenersPersistence.ViewListener;

public class UserController implements ViewListener {

    private FrameController frameController;
    private AccountManager accountManager;
    InitialController initialController;


    public UserController (FrameController frameController, AccountManager accountManager, InitialController initialController) {
        this.frameController = frameController;
        this.accountManager = accountManager;
        this.initialController = initialController;
    }

    private void logout() {
        accountManager.logout();

        initialController.startProgram();
    }

    @Override
    public void setAction(ButtonEnumeration action) {
        switch (action) {
            case LOGOUT -> logout();
        }
    }
}
