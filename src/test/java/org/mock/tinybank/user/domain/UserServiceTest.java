package org.mock.tinybank.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.common.UserRecord;
import org.mock.tinybank.user.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userService = new UserService();
    }

    @Test
    void createUser() {
        UserRecord user = new UserRecord("somePerson");
        UserRecord actual = userService.createUser(user);
        assertThat(actual).isEqualTo(user);
    }

    @Test
    void getUser() throws EntityNotFoundException {
        UserRecord user = new UserRecord("somePerson");
        userService.createUser(user);
        assertThat(userService.getUser("somePerson")).isEqualTo(user);
    }

    @Test
    void getUser_doesntExist() throws EntityNotFoundException {
        UserRecord user = new UserRecord("somePerson");
        userService.createUser(user);
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> userService.getUser("nonExistentUser"));
    }


    @Test
    void deactivateUser() {
        UserRecord user = new UserRecord("somePerson");
        userService.createUser(user);
        UserRecord actual = userService.deactivateUser("somePerson");
        assertThat(actual).isEqualTo(user);
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> userService.getUser("somePerson"));
    }
}