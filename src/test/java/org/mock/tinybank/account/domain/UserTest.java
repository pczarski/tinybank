package org.mock.tinybank.account.domain;

import org.junit.jupiter.api.Test;
import org.mock.tinybank.account.domain.model.AccountTransaction;
import org.mock.tinybank.account.domain.model.TransactionType;
import org.mock.tinybank.account.domain.model.User;

import java.math.BigInteger;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    void getBalance_EmptyTransactions() {
        User user = new User(emptyList());
        assertThat(user.getBalance()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    void getBalance_MultipleTransactions() {
        List<AccountTransaction> transactions = asList(
                new MockAccountTransaction(BigInteger.valueOf(50), TransactionType.DEPOSIT),
                new MockAccountTransaction(BigInteger.valueOf(-25), TransactionType.WITHDRAWAL),
                new MockAccountTransaction(BigInteger.valueOf(20), TransactionType.TRANSFER));
        User user = new User(transactions);
        assertThat(user.getBalance()).isEqualTo(BigInteger.valueOf(45));
    }

    private record MockAccountTransaction(BigInteger netUnits,
                                          TransactionType transactionType) implements AccountTransaction {
    }
}