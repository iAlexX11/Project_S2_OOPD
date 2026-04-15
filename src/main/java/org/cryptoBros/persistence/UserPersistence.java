package org.cryptoBros.persistence;

import org.cryptoBros.business.User;

public interface UserPersistence {
    void addUser(User user);
    void removeUser(int id);
    void getUser(String username, String email);
}
