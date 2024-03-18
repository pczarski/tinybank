package org.mock.tinybank.dto;

import java.util.Objects;

public record UserDto(String username) {
    public UserDto {
        Objects.requireNonNull(username);
    }
}
