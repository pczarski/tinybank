package org.mock.tinybank.domain;

import java.math.BigInteger;

public record AccountTransactionOutgoingTransfer(BigInteger netUnits, TransactionType transactionType,
                                                 String receiver) implements AccountTransaction {
}
