package org.mock.tinybank.account.persistence.Dao;

import org.mock.tinybank.account.domain.model.TransactionType;

import java.math.BigInteger;

public record TransactionDao(String fromUser, String toUser, BigInteger units, TransactionType transactionType) {
}
