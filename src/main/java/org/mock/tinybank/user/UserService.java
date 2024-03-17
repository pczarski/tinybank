package org.mock.tinybank.user;

import org.mock.tinybank.persistence.EntityNotFoundException;
import org.mock.tinybank.persistence.KeyValueStore;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final KeyValueStore<String, UserDto> users = new KeyValueStore<>();
    public UserDto createUser(UserDto user) {
        return users.put(user.userName(), user);
    }

    public UserDto getUser(String userName) throws EntityNotFoundException {
        return users.get(userName);
    }
}
