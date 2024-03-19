package org.mock.tinybank.account.domain.model;

import java.math.BigInteger;

public record IncomingTransfer(BigInteger netUnits,
                               String sender) implements AccountTransaction {
}
