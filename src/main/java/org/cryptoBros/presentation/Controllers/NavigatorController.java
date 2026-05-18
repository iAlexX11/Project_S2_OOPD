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
	private final AdminController adminController;
	private boolean isAdmin = false;

    public NavigatorController(FrameController frameController, UserController userController, AdminController adminController) {
        this.frameController = frameController;
        this.userController = userController;
		this.adminController = adminController;
        this.cryptoMarketController = new CryptoMarketController(userController, adminController, this);
        this.settingController = new SettingController(userController, this, adminController);
        this.portfolioController = new PortfolioController(userController, this, adminController);

        userController.registerBalanceListener(cryptoMarketController);
        userController.pushCurrentBalance(cryptoMarketController);
        frameController.displayContent(cryptoMarketController.getView(isAdmin));
    }

	public void setUSerType(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	// TODO: MOVE EVERYTHING TO THE CORRESPONDING CONTROLLER
    @Override
    public void navigate(PagesName name) {
        switch (name) {
            case SETTING -> {
                frameController.displayContent(settingController.getView(isAdmin));
            }
            case CRYPTO_MARKET ->  {
				frameController.displayContent(cryptoMarketController.getView(isAdmin));
            }
            case PORTFOLIO ->  {
                frameController.displayContent(portfolioController.getView(isAdmin));
            }
        }
    }
}
