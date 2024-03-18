package org.mock.tinybank.domain;

import java.math.BigInteger;

// this could be further broken down into withdrawal and deposit with DEPOSIT and WITHDRAWAL hard coded in each respectively
public record AccountTransactionWithdrawalOrDeposit(BigInteger netUnits,
                                                    TransactionType transactionType) implements AccountTransaction {
}
