package org.mock.tinybank.domain;

import java.math.BigInteger;

public record AccountTransactionWithdrawalOrDeposit(BigInteger netUnits,
                                                    TransactionType transactionType) implements AccountTransaction {
}
