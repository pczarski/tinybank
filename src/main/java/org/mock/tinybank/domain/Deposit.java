package org.mock.tinybank.domain;

import java.math.BigInteger;

public record Deposit(BigInteger netUnits,
                      TransactionType transactionType) implements AccountTransaction {
}
