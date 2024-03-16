package org.mock.tinybank.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userService = new UserService();
    }

    @Test
    void createUser() {
        User user = new User("somePerson");
        User actual = userService.createUser(user);
        assertThat(actual).isEqualTo(user);
    }
}