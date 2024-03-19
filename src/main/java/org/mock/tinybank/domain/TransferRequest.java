package org.mock.tinybank.domain;

import java.math.BigInteger;

public record TransferRequest(String fromUser, String toUser, BigInteger units) {
}
