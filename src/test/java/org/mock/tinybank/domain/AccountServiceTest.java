package org.mock.tinybank.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.TransactionDto;
import org.mock.tinybank.dto.UserDto;
import org.mock.tinybank.persistence.TransactionPersistenceService;

import java.math.BigInteger;
import java.util.List;

import static java.math.BigInteger.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mock.tinybank.domain.TransactionType.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    private final UserService userService = mock(UserService.class);
    private final TransactionPersistenceService transactionPersistenceService = mock(TransactionPersistenceService.class);
    private AccountService accountService;
    private final String username = "banker_man";
    private final UserDto user = new UserDto(username);

    @BeforeEach
    void beforeEach() {
        accountService = new AccountService(userService, transactionPersistenceService);
    }

    @Test
    void deposit() {
        TransactionDto depositTransaction = new TransactionDto("DEPOSIT_POINT", username, TWO, DEPOSIT);

        when(userService.getUser(username)).thenReturn(user);
        when(transactionPersistenceService.addTransaction(depositTransaction)).thenReturn(depositTransaction);

        AccountAmountDto deposit = new AccountAmountDto(username, TWO);
        AccountAmountDto actual = accountService.deposit(deposit);

        assertThat(actual).isEqualTo(deposit);
    }

    @Test
    void getBalance_Empty() {
        when(userService.getUser(username)).thenReturn(user);
        when(transactionPersistenceService.getTransactions(username)).thenReturn(emptyList());
        BigInteger actual = accountService.getBalance(username);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void getBalance_WithTransactions() {
        when(userService.getUser(username)).thenReturn(user);
        when(transactionPersistenceService.getTransactions(username)).thenReturn(getMockTransactions());
        BigInteger actual = accountService.getBalance(username);
        assertThat(actual).isEqualTo(7);
    }

    @Test
    void withdraw_InsufficientBalance() {
        when(userService.getUser(username)).thenReturn(user);
        when(transactionPersistenceService.getTransactions(username)).thenReturn(getMockTransactions());

        AccountAmountDto withdrawal = new AccountAmountDto(username, BigInteger.valueOf(9999));
        assertThatExceptionOfType(InsufficientBalanceException.class)
                .isThrownBy(() -> accountService.withdraw(withdrawal));
    }

    @Test
    void withdraw() {
        TransactionDto withdrawTransaction = new TransactionDto(username, "WITHDRAWAL_POINT", TWO, WITHDRAWAL);

        when(transactionPersistenceService.getTransactions(username)).thenReturn(getMockTransactions());
        when(userService.getUser(username)).thenReturn(user);
        when(transactionPersistenceService.addTransaction(withdrawTransaction)).thenReturn(withdrawTransaction);

        AccountAmountDto withdrawal = new AccountAmountDto(username, TWO);
        AccountAmountDto actual = accountService.withdraw(withdrawal);

        assertThat(actual).isEqualTo(withdrawal);
    }

    private List<TransactionDto> getMockTransactions() {
        return List.of(
                new TransactionDto("DEPOSIT_POINT", username, TEN, DEPOSIT),
                new TransactionDto(username, "WITHDRAWAL_POINT", TWO, WITHDRAWAL),
                new TransactionDto(username, "other_user", TWO, TRANSFER),
                new TransactionDto("other_user", username, ONE, TRANSFER)
                // 10 - 2 - 2 + 1 = 7
        );
    }
}