package org.cryptoBros.business;
import org.passay.*;
import org.mindrot.jbcrypt.BCrypt;
import static org.passay.EnglishCharacterData.*;

public class AccountManager {

	public boolean checkPassword(char[] password) {

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
		data = null;

		return result.isValid();
	}

	public String hashPassword(char[] password) {
		return BCrypt.hashpw(new String(password), BCrypt.gensalt(12));
	}

	public boolean checkHashedPassword(char[] password,  String hashedPassword) {
		return BCrypt.checkpw(new String(password), hashedPassword);
	}

	public boolean checkEmail(String email) {
		return email.matches("^(?![.])[A-Za-z0-9+_-]+(\\.[A-Za-z0-9+_-]+)*@[A-Za-z0-9]+(-[A-Za-z0-9]+)*(\\.[A-Za-z0-9]+(-[A-Za-z0-9]+)*)+$");
	}


    public void logout() {
        //TODO: Remove the current user from ram
    }
}