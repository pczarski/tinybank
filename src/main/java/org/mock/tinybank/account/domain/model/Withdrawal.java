package org.mock.tinybank.account.domain.model;

import java.math.BigInteger;

public record Withdrawal(BigInteger netUnits) implements AccountTransaction {
}
