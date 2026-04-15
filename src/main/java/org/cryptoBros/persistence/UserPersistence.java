package org.cryptoBros.persistence;

import org.cryptoBros.business.User;

public interface UserPersistence {
    void addUser(User user);
    void removeUser(int id);
    User getUser(String username, String email);
}
