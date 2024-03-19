package org.mock.tinybank.common;

import java.util.Objects;

public record UserRecord(String username) {
    public UserRecord {
        Objects.requireNonNull(username);
    }
}
