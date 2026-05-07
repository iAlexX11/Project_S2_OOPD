package org.cryptoBros.persistence;

import org.cryptoBros.business.User;
import org.cryptoBros.persistence.Exceptions.DbConnectionException;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;

public interface UserPersistence {
    /**
     * Adds a user to the db.
     * @param user the user without the id
     * @throws UserNotAddException if the user could not be added to the db.
     * @throws DbConnectionException if there was an error connecting to the db.
     * @return the user with the generated id
     */
    User addUser(User user) throws UserNotAddException, DbConnectionException;

    /**
     * Removes a user from the db.
     * @param id the id of the user to be removed
     * @throws UserNotFoundException if there is no user with the given id in the db.
     * @throws DbConnectionException if there was an error connecting to the db.
     */
    void removeUser(int id ) throws UserNotFoundException, DbConnectionException;

    /**
     * Gets a user from the db.
     * @param username the username of the user to be retrieved
     * @param email the email of the user to be retrieved
     * @return the user with the given username and email
     * @throws UserNotFoundException if there is no user with the given username or email in the db.
     * @throws DbConnectionException if there was an error connecting to the db.
     */
    User getUser(String username, String email) throws UserNotFoundException, DbConnectionException;

	/**
	 * Gets a user from the db.
	 * @param id the id of the user to be retrieved
	 * @return the user with the given username and email
	 * @throws UserNotFoundException if there is no user with the given username or email in the db.
	 * @throws DbConnectionException if there was an error connecting to the db.
	 */
	double getUserBalance(int id) throws UserNotFoundException, DbConnectionException;
}
