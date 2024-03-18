package org.mock.tinybank.domain;

import java.math.BigInteger;

import static org.mock.tinybank.domain.TransactionType.TRANSFER;

public record AccountTransactionIncomingTransfer(BigInteger netUnits, String sender) implements AccountTransaction {
    public TransactionType transactionType() {
        return TRANSFER;
    }
}
