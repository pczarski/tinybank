package org.mock.tinybank.domain;

import java.math.BigInteger;

public record AccountAmountRequest(String username, BigInteger units) {
}
