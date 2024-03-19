package org.mock.tinybank.domain;

import java.math.BigInteger;

public record OutgoingTransfer(BigInteger netUnits,
                               String receiver) implements AccountTransaction {
}
