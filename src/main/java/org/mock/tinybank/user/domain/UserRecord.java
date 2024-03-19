package org.mock.tinybank.user.domain;

import java.util.Objects;

public record UserRecord(String username) {
    public UserRecord {
        Objects.requireNonNull(username);
    }
}
