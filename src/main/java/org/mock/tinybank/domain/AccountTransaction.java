package org.mock.tinybank.domain;

import java.math.BigInteger;

public record AccountTransaction(BigInteger netUnits, TransactionType transactionType) {
}
