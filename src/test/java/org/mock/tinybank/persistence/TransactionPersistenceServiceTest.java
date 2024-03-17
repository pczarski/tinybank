package org.mock.tinybank.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.domain.TransactionType;
import org.mock.tinybank.dto.TransactionDto;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mock.tinybank.domain.TransactionType.DEPOSIT;

class TransactionPersistenceServiceTest {

    private TransactionPersistenceService transactionPersistenceService;

    @BeforeEach
    void beforeEach() {
        transactionPersistenceService = new TransactionPersistenceService();
    }

    @Test
    void addTransaction() {
        TransactionDto toAdd = new TransactionDto("from_me", "to_you", BigInteger.TEN, DEPOSIT);
        TransactionDto actual = transactionPersistenceService.addTransaction(toAdd);
        assertThat(actual).isEqualTo(toAdd);
    }

    @Test
    void getTransactions_ReturnsCorrectTransactions() {
        TransactionDto myTransaction1 = new TransactionDto("this_is_me", "this_is_not_me", BigInteger.TEN, TransactionType.DEPOSIT);
        TransactionDto myTransaction2 = new TransactionDto("also_not_me", "this_is_me", BigInteger.valueOf(20), TransactionType.WITHDRAW);
        TransactionDto transaction3 = new TransactionDto("not_me", "not_me2", BigInteger.valueOf(20), TransactionType.WITHDRAW);
        transactionPersistenceService.addTransaction(myTransaction1);
        transactionPersistenceService.addTransaction(myTransaction2);
        transactionPersistenceService.addTransaction(transaction3);

        List<TransactionDto> myTransactions = transactionPersistenceService.getTransactions("this_is_me");

        assertThat(myTransactions).hasSize(2).containsAll(List.of(myTransaction1, myTransaction2));
    }

    @Test
    void getTransactions_ReturnsEmptyListForNonexistentUser() {
        TransactionDto transaction1 = new TransactionDto("some_person", "another_person", BigInteger.TEN, TransactionType.DEPOSIT);
        transactionPersistenceService.addTransaction(transaction1);

        List<TransactionDto> nonExistentUserTransactions = transactionPersistenceService.getTransactions("not_existing_person");

        assertThat(nonExistentUserTransactions).isEmpty();
    }
}