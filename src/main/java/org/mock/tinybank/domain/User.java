package org.mock.tinybank.domain;

import java.math.BigInteger;
import java.util.List;

public class User {
    private final List<AccountTransaction> transactions;

    public User(List<AccountTransaction> transactions) {
        this.transactions = transactions;
    }

    // todo test
    public BigInteger getBalance() {
        return transactions.stream()
                .map(AccountTransaction::netUnits)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

}
