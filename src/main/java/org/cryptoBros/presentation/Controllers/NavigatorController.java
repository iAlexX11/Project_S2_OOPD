package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.Pages;

public class NavigatorController implements Navigation {

    private final CryptoMarketController cryptoMarketController;
    private final FrameController frameController;
    private final SettingController settingController;
    private final PortfolioController portfolioController;
    private final UserController userController;

    public NavigatorController(FrameController frameController, UserController userController) {
        this.frameController = frameController;
        this.userController = userController;

        userController.initCrypto();

        this.cryptoMarketController = new CryptoMarketController(userController, this);
        this.settingController = new SettingController(userController, this);
        this.portfolioController = new PortfolioController(userController, this);

        userController.registerCryptoListener(cryptoMarketController);
        userController.getAllCrypto();
        userController.registerBalanceListener(cryptoMarketController);
        userController.pushCurrentBalance(cryptoMarketController);
        frameController.displayContent(cryptoMarketController.getView());
    }


    @Override
    public void navigate(PagesName name) {
        userController.removeCryptoListener();

        switch (name) {
            case SETTING -> {
                userController.registerBalanceListener(settingController);
                userController.pushCurrentBalance(settingController);
                frameController.displayContent(settingController.getView());
            }
            case CRYPTO_MARKET ->  {
                userController.registerCryptoListener(cryptoMarketController);
                cryptoMarketController.clearTable();
                userController.getAllCrypto();
                userController.registerBalanceListener(cryptoMarketController);
                userController.pushCurrentBalance(cryptoMarketController);
                frameController.displayContent(cryptoMarketController.getView());
            }
            case PORTFOLIO ->  {
                userController.registerBalanceListener(portfolioController);
                userController.pushCurrentBalance(portfolioController);
                frameController.displayContent(portfolioController.getView());
            }
        }
    }
}
