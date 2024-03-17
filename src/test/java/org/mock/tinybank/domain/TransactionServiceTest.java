package org.mock.tinybank.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.dto.DepositWithdrawDto;
import org.mock.tinybank.dto.UserDto;
import org.mock.tinybank.persistence.TransactionPersistenceService;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    private final UserService userService = mock(UserService.class);
    private TransactionService transactionService;

    @BeforeEach
    void beforeEach() {
        transactionService = new TransactionService(userService, new TransactionPersistenceService());
    }

    @Test
    void deposit() {
        String userName = "banker_man";
        UserDto user = new UserDto(userName);
        when(userService.getUser(userName)).thenReturn(user);
        DepositWithdrawDto deposit = new DepositWithdrawDto(userName, BigInteger.TWO);
        DepositWithdrawDto actual = transactionService.deposit(deposit);
        assertThat(actual).isEqualTo(deposit);
    }
}