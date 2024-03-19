package org.mock.tinybank.account.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.mock.tinybank.account.domain.model.TransactionType;

import java.math.BigInteger;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionDto(TransactionType transactionType, BigInteger netUnits, String sender, String receiver) {
}
