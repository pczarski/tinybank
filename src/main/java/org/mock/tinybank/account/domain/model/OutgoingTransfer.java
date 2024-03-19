package org.mock.tinybank.account.domain.model;

import java.math.BigInteger;

public record OutgoingTransfer(BigInteger netUnits,
                               String receiver) implements AccountTransaction {
}
