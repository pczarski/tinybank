package org.mock.tinybank.domain;

import java.math.BigInteger;

public record Withdrawal(BigInteger netUnits,
                         TransactionType transactionType) implements AccountTransaction {
}
