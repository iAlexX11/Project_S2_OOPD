package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;


public class NavigatorController implements Navigation {

    private final CryptoMarketController cryptoMarketController;
    private final FrameController frameController;
    private final SettingController settingController;
    private final PortfolioController portfolioController;
	private final ManageCryptoController manageCryptoController;
    private final UserController userController;
	private final AdminController adminController;
	private boolean isAdmin = false;

    public NavigatorController(FrameController frameController, UserController userController, AdminController adminController, boolean isAdmin) {
        this.frameController = frameController;
        this.userController = userController;
		this.adminController = adminController;
        this.cryptoMarketController = new CryptoMarketController(userController, adminController, this, isAdmin);
        this.settingController = new SettingController(userController, this, adminController, isAdmin);
        this.portfolioController = new PortfolioController(userController, this, adminController);
		this.manageCryptoController = new ManageCryptoController(frameController, this, adminController, isAdmin);
        frameController.displayContent(cryptoMarketController.getView());
		this.isAdmin = isAdmin;
    }

    @Override
    public void navigate(PagesName name) {
        switch (name) {
            case SETTING -> {
                frameController.displayContent(settingController.getView());
            }
            case CRYPTO_MARKET ->  {
				frameController.displayContent(cryptoMarketController.getView());
            }
            case PORTFOLIO ->  {
                frameController.displayContent(portfolioController.getView());
            }
			case MANAGE_CRYPTO ->  {
				frameController.displayContent(manageCryptoController.getView());
			}
        }
    }
}
