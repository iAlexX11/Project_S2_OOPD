package org.cryptoBros.business;
import org.cryptoBros.presentation.Controllers.UserController;
import org.passay.*;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.passay.EnglishCharacterData.*;

public class AccountManager {

	public String signUpLogic(String email, char[] password, char[] confirmPassword, String username) {
		String errorCredentials = checkCredentials(email, password, confirmPassword);
		if (!errorCredentials.equals("ok")) {
			return errorCredentials;
		}

		String hashedPassword = hashPassword(password);
		UserManager userManager = new UserManager();
		String error;

		if (userManager.getUser(username, email) == null) {
			User user = new User(username, email, hashedPassword);
			User userWithId = userManager.addUser(user);
			userManager.setCurrentUserId(userWithId.getId());
			error = ("ok");
		} else {
			error = ("This email/username already exists!");
		}

		return error;

	}

	private String checkCredentials(String email, char[] password, char[] confirmPassword) {
		List<String> errors = new ArrayList<>();

		if (!checkEmail(email)) {
			errors.add("Error email format");
		}

		if (!Arrays.equals(password, confirmPassword)) {
			errors.add(("The password doesn't match"));
		} else {

			String passwordCheck = checkPassword(password);
			if (!passwordCheck.equals("ok")) {
				errors.add(passwordCheck);
			}
		}

		return errors.isEmpty() ? "ok" : String.join("\n", errors);
	}

	private String checkPassword(char[] password) {
		String passwordFormat = ("Password must be 6–20 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character. Spaces are not allowed.");

		PasswordValidator validator = new PasswordValidator(
				new LengthRule(6, 20),
				new CharacterRule(UpperCase, 1),
				new CharacterRule(LowerCase, 1),
				new CharacterRule(Digit, 1),
				new CharacterRule(Special, 1),
				new WhitespaceRule()
		);

		PasswordData data = new PasswordData(new String(password));
		RuleResult result = validator.validate(data);

		if (result.isValid()) {
			return "ok";
		}

		return passwordFormat;
	}

	public String hashPassword(char[] password) {
		return BCrypt.hashpw(new String(password), BCrypt.gensalt(12));
	}

	public boolean checkHashedPassword(char[] password,  String hashedPassword) {
		return BCrypt.checkpw(new String(password), hashedPassword);
	}

	private boolean checkEmail(String email) {
		return email.matches("^(?![.])[A-Za-z0-9+_-]+(\\.[A-Za-z0-9+_-]+)*@[A-Za-z0-9]+(-[A-Za-z0-9]+)*(\\.[A-Za-z0-9]+(-[A-Za-z0-9]+)*)+$");
	}

	public String logInNormalUser(String usernameOrEmail, char[] password) {
		UserManager userManager = new UserManager();
		User possibleUser = userManager.getUser(usernameOrEmail, usernameOrEmail);

		if (possibleUser != null && checkHashedPassword(password, possibleUser.getPassword())) {
			userManager.setCurrentUserId(possibleUser.getId());
			return ("ok");

		} else {
			return ("This email/username doesn't exist!");
		}
	}
}