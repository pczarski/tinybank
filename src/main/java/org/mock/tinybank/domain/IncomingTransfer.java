package org.mock.tinybank.domain;

import java.math.BigInteger;

public record IncomingTransfer(BigInteger netUnits,
                               String sender) implements AccountTransaction {
}
