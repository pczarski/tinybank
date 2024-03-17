package org.mock.tinybank.dto;

import java.math.BigInteger;

public record DepositWithdrawDto(String userName, BigInteger units) {
}
