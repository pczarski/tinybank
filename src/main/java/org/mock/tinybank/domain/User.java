package org.mock.tinybank.domain;


import java.math.BigInteger;
import java.util.List;

public record User(List<AccountTransaction> transactions) {

    public BigInteger getBalance() {
        return transactions.stream()
                .map(AccountTransaction::netUnits)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

}
