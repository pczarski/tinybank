package org.mock.tinybank.user.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserRecordTest {
    @Test
    void usernameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new UserRecord(null));
    }
}