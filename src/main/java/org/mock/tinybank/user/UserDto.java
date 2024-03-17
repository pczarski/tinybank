package org.mock.tinybank.user;

import java.util.Objects;

public record UserDto(String userName) {
    public UserDto {
        Objects.requireNonNull(userName);
    }
}
