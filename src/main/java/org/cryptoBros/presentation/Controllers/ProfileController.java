package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.ProfileView;
import org.cryptoBros.presentation.Views.BaseView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileController implements ActionListener, BalanceListener {

	private final UserController userController;
	private final Navigation navigation;
	private final ProfileView profileView;
	private final AdminController adminController;

	public ProfileController(UserController userController, Navigation navigation, AdminController adminController, boolean	isAdmin) {
		this.userController = userController;
		this.navigation = navigation;
		this.profileView = new ProfileView();
		this.adminController = adminController;
		profileView.setTypeUser(isAdmin);
		profileView.setActions(this);
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
			case CHANGE_PASSWORD -> userController.changeUserPassword(profileView.getPassword());
			case CHANGE_PROFILE_PIC -> System.out.println("Change password");
			case CHANGE_USERNAME -> userController.changeUsername(profileView.getUsername());
		}
	}

	@Override
	public void balanceChanged(double balance) {
		profileView.updateBalance(balance);
	}

	public BaseView getView() {
		return profileView;
	}
}


