package org.cryptoBros.persistence;

import org.cryptoBros.business.User;

public interface UserPersistence {
    /**
     *
     * @param user the user without the id
     * @return the user with the generated id
     */
    User addUser(User user);
    void removeUser(int id);
    User getUser(String username, String email);
	double getUserBalance(String username, String email);
}
