package org.mock.tinybank.dto;

import java.math.BigInteger;

public record TransactionDto(String fromUser, String toUser, BigInteger units) {};
