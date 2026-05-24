package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.Pages;
import org.cryptoBros.presentation.Views.SettingsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controls the settings view with logout, profile, and account deletion options.
 */
public class SettingController implements ActionListener, BalanceListener {

    private final UserController userController;
    private final Navigation navigation;
    private final SettingsView settingsView;

    /**
     * Creates a new SettingController.
     *
     * @param userController  the controller for user operations
     * @param navigation      the navigation handler
     * @param adminController the controller for admin operations
     * @param isAdmin         true if the current user is an admin
     */
    public SettingController(UserController userController, Navigation navigation, AdminController adminController, boolean	isAdmin) {
        this.userController = userController;
        this.navigation = navigation;
        this.settingsView = new SettingsView(isAdmin);
		settingsView.init();
		settingsView.setTypeUser(isAdmin);
        settingsView.setActions(this);
		if (!isAdmin) {
			userController.registerBalanceListener(this);
			userController.pushCurrentBalance(this);
		}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
        switch (buttonEnumeration) {
            case SETTINGS -> navigation.navigate(PagesName.SETTING);
            case HOME -> navigation.navigate(PagesName.CRYPTO_MARKET);
            case PORTFOLIO -> navigation.navigate(PagesName.PORTFOLIO);
            case LOGOUT -> userController.logout();
            case PROFILE -> navigation.navigate(PagesName.PROFILE);
            case DELETE -> userController.deleteUser();
			case MANAGE_CRYPTO ->  navigation.navigate(PagesName.MANAGE_CRYPTO);

        }
    }

    @Override
    public void balanceChanged(double balance) {
        settingsView.updateBalance(balance);
        settingsView.updateEstimatedProfit(userController.getTotalProfit());
    }

    /**
     * Returns the settings view.
     *
     * @return the settings view
     */
    public BaseView getView() {
        return settingsView;
    }
}
