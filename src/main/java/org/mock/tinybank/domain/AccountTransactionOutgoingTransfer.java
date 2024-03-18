package org.mock.tinybank.domain;

import java.math.BigInteger;

import static org.mock.tinybank.domain.TransactionType.TRANSFER;

public record AccountTransactionOutgoingTransfer(BigInteger netUnits, String receiver) implements AccountTransaction {
    public TransactionType transactionType() {
        return TRANSFER;
    }
}
