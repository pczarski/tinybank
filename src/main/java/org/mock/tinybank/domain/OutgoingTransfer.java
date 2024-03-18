package org.mock.tinybank.domain;

import java.math.BigInteger;

public record OutgoingTransfer(BigInteger netUnits, TransactionType transactionType,
                               String receiver) implements AccountTransaction {
}
