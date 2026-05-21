package org.cryptoBros.persistence.Exceptions;

public class CryptoNameAlreadyExists extends RuntimeException {
	public CryptoNameAlreadyExists(String message) {
		super(message);
	}
}
