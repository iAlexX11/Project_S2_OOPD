package org.cryptoBros.presentation.Controllers;

import org.cryptoBros.business.Liseners.BalanceListener;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;
import org.cryptoBros.presentation.Enum.PagesName;
import org.cryptoBros.presentation.ListenersPersistence.Navigation;
import org.cryptoBros.presentation.Views.AccountView;
import org.cryptoBros.presentation.Views.BaseView;
import org.cryptoBros.presentation.Views.SettingsView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileController implements ActionListener, BalanceListener {

	private final UserController userController;
	private final Navigation navigation;
	private final AccountView accountView;
	private final AdminController adminController;

	public ProfileController(UserController userController, Navigation navigation, AdminController adminController, boolean	isAdmin) {
		this.userController = userController;
		this.navigation = navigation;
		this.accountView = new AccountView();
		this.adminController = adminController;
		accountView.setTypeUser(isAdmin);
		accountView.setActions(this);
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
			case CHANGE_PASSWORD -> accountView.showChangePassword();
			case CHANGE_PROFILE_PIC -> accountView.showChangeProfilePicture();
			case CHANGE_USERNAME -> accountView.showChangeUsername();
		}
	}

	@Override
	public void balanceChanged(double balance) {
		accountView.updateBalance(balance);
	}

	public BaseView getView() {
		return accountView;
	}
}


