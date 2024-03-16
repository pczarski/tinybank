package org.mock.tinybank.user;

import org.springframework.stereotype.Service;

import java.util.Hashtable;

@Service
public class UserService {
    private final Hashtable<String, User> users = new Hashtable<>();
    public User createUser(User user) {
        users.put(user.userName(), user);
        return users.get(user.userName());
    }
}
