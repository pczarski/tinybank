package org.mock.tinybank.persistence;

import org.mock.tinybank.domain.TransactionType;

import java.math.BigInteger;

public record TransactionDao(String fromUser, String toUser, BigInteger units, TransactionType transactionType) {
}
