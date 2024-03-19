package org.mock.tinybank.domain;

import java.math.BigInteger;

public record Withdrawal(BigInteger netUnits) implements AccountTransaction {
}
