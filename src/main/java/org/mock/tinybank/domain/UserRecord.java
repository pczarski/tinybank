package org.mock.tinybank.domain;

import java.util.Objects;

public record UserRecord(String username) {
    public UserRecord {
        Objects.requireNonNull(username);
    }
}
