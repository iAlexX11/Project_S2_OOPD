package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.Pages;

public class NavigatorController implements Navigation {

    private final CryptoMarketController cryptoMarketController;
    private final FrameController frameController;
    private final SettingController settingController;
    private final UserController userController;

    public NavigatorController(FrameController frameController, UserController userController) {
        this.frameController = frameController;
        this.userController = userController;
        this.cryptoMarketController = new CryptoMarketController(userController, this);
        this.settingController = new SettingController(userController, this);

        userController.registerBalanceListener(cryptoMarketController);
        userController.pushCurrentBalance(cryptoMarketController);
        frameController.displayContent(cryptoMarketController.getView());
    }


    @Override
    public void navigate(PagesName name) {
        switch (name) {
            case SETTING -> {
                userController.registerBalanceListener(settingController);
                userController.pushCurrentBalance(settingController);
                frameController.displayContent(settingController.getView());
            }
            case CRYPTO_MARKET ->  {
                userController.registerBalanceListener(cryptoMarketController);
                userController.pushCurrentBalance(cryptoMarketController);
                frameController.displayContent(cryptoMarketController.getView());
            }
        }
    }
}
