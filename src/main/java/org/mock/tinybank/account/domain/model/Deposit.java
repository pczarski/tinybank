package org.mock.tinybank.account.domain.model;

import java.math.BigInteger;

public record Deposit(BigInteger netUnits) implements AccountTransaction {
}
