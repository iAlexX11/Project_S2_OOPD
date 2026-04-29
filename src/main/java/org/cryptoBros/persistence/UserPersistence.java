package org.cryptoBros.persistence;

import org.cryptoBros.business.User;
import org.cryptoBros.persistence.Exceptions.UserNotAddException;
import org.cryptoBros.persistence.Exceptions.UserNotFoundException;

public interface UserPersistence {
    /**
     *
     * @param user the user without the id
     * @return the user with the generated id
     */
    User addUser(User user) throws UserNotAddException;
    void removeUser(int id ) throws UserNotFoundException;
    User getUser(String username, String email) throws UserNotFoundException;
}
