package org.mock.tinybank.domain;

import org.mock.tinybank.dto.UserDto;
import org.mock.tinybank.persistence.EntityNotFoundException;
import org.mock.tinybank.persistence.KeyValueStore;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final KeyValueStore<String, UserDto> users = new KeyValueStore<>();
    public UserDto createUser(UserDto user) {
        return users.put(user.username(), user);
    }

    public UserDto getUser(String username) throws EntityNotFoundException {
        return users.get(username);
    }

    public UserDto deactivateUser(String username) {
        return users.delete(username);
    }
}
