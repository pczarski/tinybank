package org.mock.tinybank.api.dto;

import lombok.Builder;
import org.mock.tinybank.domain.TransactionType;

import java.math.BigInteger;

@Builder
public record TransactionDto(TransactionType transactionType, BigInteger netUnits, String sender, String receiver) {
}
