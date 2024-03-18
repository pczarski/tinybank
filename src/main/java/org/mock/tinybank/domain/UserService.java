package org.mock.tinybank.domain;

import org.mock.tinybank.persistence.EntityNotFoundException;
import org.mock.tinybank.persistence.KeyValueStore;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final KeyValueStore<String, UserRecord> users = new KeyValueStore<>();

    public UserRecord createUser(UserRecord user) {
        return users.put(user.username(), user);
    }

    public UserRecord getUser(String username) throws EntityNotFoundException {
        return users.get(username);
    }

    // todo test
    public UserRecord deactivateUser(String username) {
        return users.delete(username);
    }
}
