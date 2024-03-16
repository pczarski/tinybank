package org.mock.tinybank.user;

import java.util.Objects;

public record User(String userName) {
    public User {
        Objects.requireNonNull(userName);
    }
}
