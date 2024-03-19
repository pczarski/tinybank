package org.mock.tinybank.account.domain.model;

import java.math.BigInteger;

public record AccountAmountRequest(String username, BigInteger units) {
}
