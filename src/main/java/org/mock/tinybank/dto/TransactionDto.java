package org.mock.tinybank.dto;

import org.mock.tinybank.domain.TransactionType;

import java.math.BigInteger;

public record TransactionDto(String fromUser, String toUser, BigInteger units, TransactionType transactionType) {
};
