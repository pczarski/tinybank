package org.mock.tinybank.account.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.account.domain.model.TransactionType;
import org.mock.tinybank.account.persistence.Dao.TransactionDao;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mock.tinybank.account.domain.model.TransactionType.DEPOSIT;

class TransactionPersistenceServiceTest {

    private TransactionPersistenceService transactionPersistenceService;

    @BeforeEach
    void beforeEach() {
        transactionPersistenceService = new TransactionPersistenceService();
    }

    @Test
    void addTransaction() {
        TransactionDao toAdd = new TransactionDao("from_me", "to_you", BigInteger.TEN, DEPOSIT);
        TransactionDao actual = transactionPersistenceService.addTransaction(toAdd);
        assertThat(actual).isEqualTo(toAdd);
    }

    @Test
    void getTransactions_ReturnsCorrectTransactions() {
        TransactionDao myTransaction1 = new TransactionDao("this_is_me", "this_is_not_me", BigInteger.TEN, TransactionType.DEPOSIT);
        TransactionDao myTransaction2 = new TransactionDao("also_not_me", "this_is_me", BigInteger.valueOf(20), TransactionType.WITHDRAWAL);
        TransactionDao transaction3 = new TransactionDao("not_me", "not_me2", BigInteger.valueOf(20), TransactionType.WITHDRAWAL);
        transactionPersistenceService.addTransaction(myTransaction1);
        transactionPersistenceService.addTransaction(myTransaction2);
        transactionPersistenceService.addTransaction(transaction3);

        List<TransactionDao> myTransactions = transactionPersistenceService.getTransactions("this_is_me");

        assertThat(myTransactions).hasSize(2).containsAll(List.of(myTransaction1, myTransaction2));
    }

    @Test
    void getTransactions_ReturnsEmptyListForNonexistentUser() {
        TransactionDao transaction1 = new TransactionDao("some_person", "another_person", BigInteger.TEN, TransactionType.DEPOSIT);
        transactionPersistenceService.addTransaction(transaction1);

        List<TransactionDao> nonExistentUserTransactions = transactionPersistenceService.getTransactions("not_existing_person");

        assertThat(nonExistentUserTransactions).isEmpty();
    }
}