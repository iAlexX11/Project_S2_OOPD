package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.Pages;

public class NavigatorController implements Navigation {

    private final CryptoMarketController cryptoMarketController;
    private final FrameController frameController;
    private final SettingController settingController;
    private final PortfolioController portfolioController;
	private final ManageCryptoController manageCryptoController;
    private final UserController userController;
	private final AdminController adminController;
	private final ProfileController profileController;
    private final CryptoDetailController cryptoDetailController;
	private boolean isAdmin = false;

    public NavigatorController(FrameController frameController, UserController userController, AdminController adminController, boolean isAdmin) {
        this.frameController = frameController;
        this.userController = userController;
        this.adminController = adminController;
        this.manageCryptoController = new ManageCryptoController(frameController, this, adminController, isAdmin);
        this.cryptoMarketController = new CryptoMarketController(userController,  adminController,this, isAdmin);
		this.profileController = new ProfileController(userController, this, adminController, isAdmin);
        this.settingController = new SettingController(userController, this, adminController, isAdmin);
        this.portfolioController = new PortfolioController(userController, this);
        this.cryptoDetailController = new CryptoDetailController(userController, adminController, this, isAdmin);
		this.isAdmin = isAdmin;

        userController.initCrypto();
        userController.registerCryptoListener(cryptoMarketController);
        userController.getAllCrypto();
        if (!isAdmin) {
            userController.registerBalanceListener(cryptoMarketController);
            userController.pushCurrentBalance(cryptoMarketController);
        }

        frameController.displayContent(cryptoMarketController.getView());
    }

    @Override
    public void navigate(PagesName name) {
        userController.removeCryptoListener();
        cryptoDetailController.stop();

        switch (name) {
            case SETTING -> {
                if (!isAdmin) {
                    userController.registerBalanceListener(settingController);
                    userController.pushCurrentBalance(settingController);
                }
                frameController.displayContent(settingController.getView());
            }
            case CRYPTO_MARKET ->  {
                userController.registerCryptoListener(cryptoMarketController);
                cryptoMarketController.clearTable();
                userController.getAllCrypto();
                if (!isAdmin) {
                    userController.registerBalanceListener(cryptoMarketController);
                    userController.pushCurrentBalance(cryptoMarketController);
                }
                frameController.displayContent(cryptoMarketController.getView());
            }
            case PORTFOLIO ->  {
                userController.registerCryptoListener(portfolioController);
                if (!isAdmin) {
                    userController.registerBalanceListener(portfolioController);
                    userController.pushCurrentBalance(portfolioController);
                }
                frameController.displayContent(portfolioController.getView());
            }
			case MANAGE_CRYPTO ->  {
				frameController.displayContent(manageCryptoController.getView());
			}
			case PROFILE ->  {
				frameController.displayContent(profileController.getView());
			}
            case CRYPTO_DETAIL -> {
                if (!isAdmin) {
                    userController.registerBalanceListener(portfolioController);
                    userController.pushCurrentBalance(portfolioController);
                }
                frameController.displayContent(cryptoDetailController.getView());
            }
        }
    }

    public void navigateToCryptoDetail(String type) {
        userController.removeCryptoListener();
        cryptoDetailController.displayContent(type);
        frameController.displayContent(cryptoDetailController.getView());
    }
}
