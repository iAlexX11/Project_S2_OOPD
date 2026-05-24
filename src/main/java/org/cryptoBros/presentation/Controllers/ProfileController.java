package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.ProfileView;
import org.cryptoBros.presentation.Views.BaseView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controls the profile view for username and password changes.
 */
public class ProfileController implements ActionListener, BalanceListener {

	private final UserController userController;
	private final Navigation navigation;
	private final ProfileView profileView;

	/**
	 * Creates a new ProfileController.
	 *
	 * @param userController  the controller for user operations
	 * @param navigation      the navigation handler
	 * @param adminController the controller for admin operations
	 * @param isAdmin         true if the current user is an admin
	 */
	public ProfileController(UserController userController, Navigation navigation, AdminController adminController, boolean	isAdmin) {
		this.userController = userController;
		this.navigation = navigation;
		this.profileView = new ProfileView(isAdmin);
		profileView.init();
		profileView.setTypeUser(isAdmin);
		profileView.setActions(this);
		if (!isAdmin) {
			userController.registerBalanceListener(this);
			userController.pushCurrentBalance(this);
		}
	}

	/**
	 * Handles user actions from the profile view, including navigation,
	 * password changes, and username changes.
	 *
	 * @param e the action event triggered by the user
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		ButtonEnumeration buttonEnumeration = ButtonEnumeration.valueOf(e.getActionCommand());
		switch (buttonEnumeration) {
			case SETTINGS -> navigation.navigate(PagesName.SETTING);
			case HOME -> navigation.navigate(PagesName.CRYPTO_MARKET);
			case CHANGE_PASSWORD -> userController.changeUserPassword(profileView.getPassword());
			case CHANGE_USERNAME -> userController.changeUsername(profileView.getUsername());
			case PORTFOLIO ->  navigation.navigate(PagesName.PORTFOLIO);
		}
	}

	/**
	 * Updates the displayed balance and estimated profit on the profile view
	 * when the user balance changes.
	 *
	 * @param balance the new balance value
	 */
	@Override
	public void balanceChanged(double balance) {
		profileView.updateBalance(balance);
		profileView.updateEstimatedProfit(userController.getTotalProfit());
	}

	/**
	 * Returns the profile view.
	 *
	 * @return the profile view
	 */
	public BaseView getView() {
		return profileView;
	}
}


