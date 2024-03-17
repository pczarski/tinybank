package org.mock.tinybank.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.dto.TransactionDto;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionPersistenceServiceTest {

    private TransactionPersistenceService transactionPersistenceService;

    @BeforeEach
    void beforeEach() {
        transactionPersistenceService = new TransactionPersistenceService();
    }

    @Test
    void addTransaction() {
        TransactionDto toAdd = new TransactionDto("from_me", "to_you", BigInteger.TEN);
        TransactionDto actual = transactionPersistenceService.addTransaction(toAdd);
        assertThat(actual).isEqualTo(toAdd);
    }
}