package org.mock.tinybank.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.dto.UserDto;
import org.mock.tinybank.domain.UserService;
import org.mock.tinybank.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userService = new UserService();
    }

    @Test
    void createUser() {
        UserDto user = new UserDto("somePerson");
        UserDto actual = userService.createUser(user);
        assertThat(actual).isEqualTo(user);
    }

    @Test
    void getUser() throws EntityNotFoundException {
        UserDto user = new UserDto("Frodo");
        userService.createUser(user);
        assertThat(userService.getUser("Frodo")).isEqualTo(user);
    }
}