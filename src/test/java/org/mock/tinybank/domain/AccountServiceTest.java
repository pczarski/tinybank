package org.mock.tinybank.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.UserDto;
import org.mock.tinybank.persistence.TransactionPersistenceService;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    private final UserService userService = mock(UserService.class);
    private final TransactionPersistenceService transactionPersistenceService = mock(TransactionPersistenceService.class);
    private AccountService accountService;

    @BeforeEach
    void beforeEach() {
        accountService = new AccountService(userService, transactionPersistenceService);
    }

    @Test
    void deposit() {
        this.accountService = new AccountService(userService, new TransactionPersistenceService());
        String userName = "banker_man";
        UserDto user = new UserDto(userName);
        when(userService.getUser(userName)).thenReturn(user);
        AccountAmountDto deposit = new AccountAmountDto(userName, BigInteger.TWO);
        AccountAmountDto actual = accountService.deposit(deposit);
        assertThat(actual).isEqualTo(deposit);
    }
}