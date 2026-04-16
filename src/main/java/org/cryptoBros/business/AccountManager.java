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

	public boolean checkEmail(String email) {
		boolean hasDot = false;
		boolean hasName = false; //
		boolean hasAT = false; //
		boolean hasDomain = false;
		boolean hasMail = false; // gmail, yahoo...
		int nDots = 0;
		if (!email.isEmpty()) {
			for (int i = 0; i < email.length(); i++) {
				if (email.charAt(i) == '@') {
					hasAT = true;
					i++;
					for (int j = i; j < email.length(); j++) {
						if (email.charAt(j) == '.') {
							hasDot = true;
							nDots++;
						}
						if (Character.isLetter(email.charAt(j)) && !hasDot) {
							hasMail = true;
						}
						if (Character.isLetter(email.charAt(j)) && hasDot) {
							hasDomain = true;
						}
					}
					break;
				}
				if (Character.isLetterOrDigit(email.charAt(i)) && !hasAT) {
					hasName = true;
				}
			}
			if (hasName && hasAT && hasDomain && hasMail && nDots == 1) {
				return true;
			}
		}
		return false;
	}
}	