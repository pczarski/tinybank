package org.mock.tinybank.domain;

import java.math.BigInteger;

public record AccountTransactionIncomingTransfer(BigInteger netUnits, TransactionType transactionType,
                                                 String sender) implements AccountTransaction {
}
