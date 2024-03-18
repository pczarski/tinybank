package org.mock.tinybank.domain;

import java.math.BigInteger;

public record UnitTransferRecord(String fromUser, String toUser, BigInteger units) {
}
