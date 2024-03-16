package org.mock.tinybank.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {
    @Test
    void userNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new User(null));
    }
}