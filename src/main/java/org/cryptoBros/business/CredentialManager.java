package org.cryptoBros.business;

import org.cryptoBros.persistence.ConfigJson;
import org.cryptoBros.persistence.ConfigPersistence;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.passay.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.passay.EnglishCharacterData.*;

/**
 * Manages credential validation, password hashing, and configuration loading.
 */
public class CredentialManager {
    private final ConfigPersistence configPersistence;

    /**
     * Creates a new CredentialManager with the default configuration persistence.
     */
    public CredentialManager() {
        configPersistence = new ConfigJson();
    }

    /**
     * Reads the admin password from configuration.
     *
     * @return the admin password
     * @throws ConfigFileNotFoundException if the config file is not found
     */
    public String readAdminPassword() throws ConfigFileNotFoundException {
        return configPersistence.readAdminPassword();
    }

	/**
	 * Verifies a plaintext password against a BCrypt hash.
	 *
	 * @param password       the plaintext password to verify
	 * @param hashedPassword the BCrypt hash to compare against
	 * @return {@code true} if the password matches the hash
	 */
	public boolean checkHashedPassword(char[] password,  String hashedPassword) {
		return BCrypt.checkpw(new String(password), hashedPassword);
	}

	/**
	 * Validates email format and password requirements.
	 *
	 * @param email           the email address to validate
	 * @param password        the password to validate
	 * @param confirmPassword the confirmation password to match against
	 * @return "ok" if credentials are valid, or a newline-separated list of errors
	 */
	public String checkCredentials(String email, char[] password, char[] confirmPassword) {
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

	/**
	 * Hashes a password using BCrypt.
	 *
	 * @param password the plaintext password to hash
	 * @return the BCrypt hashed password
	 */
	public String hashPassword(char[] password) {
		return BCrypt.hashpw(new String(password), BCrypt.gensalt(12));
	}

	private boolean checkEmail(String email) {
		return email.matches("^(?![.])[A-Za-z0-9+_-]+(\\.[A-Za-z0-9+_-]+)*@[A-Za-z0-9]+(-[A-Za-z0-9]+)*(\\.[A-Za-z0-9]+(-[A-Za-z0-9]+)*)+$");
	}

}
