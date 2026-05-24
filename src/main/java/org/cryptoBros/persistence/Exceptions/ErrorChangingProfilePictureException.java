package org.cryptoBros.persistence.Exceptions;

/**
 * Thrown when an error occurs while changing a profile picture.
 */
public class ErrorChangingProfilePictureException extends RuntimeException {
	/**
	 * Creates a new ErrorChangingProfilePictureException with the given detail message.
	 *
	 * @param message the detail message
	 */
	public ErrorChangingProfilePictureException(String message) {
		super(message);
	}
}
