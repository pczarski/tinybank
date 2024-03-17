package org.mock.tinybank.dto;

import java.util.Objects;

public record UserDto(String userName) {
    public UserDto {
        Objects.requireNonNull(userName);
    }
}
