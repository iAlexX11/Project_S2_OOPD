package org.cryptoBros.persistence.Exceptions;

public class UsernameAlreadyExists extends RuntimeException {
	public UsernameAlreadyExists(String message) {
		super(message);
	}
}
