package org.mock.tinybank.account.domain.model;

import java.math.BigInteger;

public record TransferRequest(String fromUser, String toUser, BigInteger units) {
}
