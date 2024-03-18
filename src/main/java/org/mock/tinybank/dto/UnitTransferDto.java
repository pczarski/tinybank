package org.mock.tinybank.dto;

import java.math.BigInteger;

public record UnitTransferDto(String fromUser, String toUser, BigInteger units) {
}
