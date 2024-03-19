package org.mock.tinybank.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.mock.tinybank.domain.TransactionType;

import java.math.BigInteger;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionDto(TransactionType transactionType, BigInteger netUnits, String sender, String receiver) {
}
