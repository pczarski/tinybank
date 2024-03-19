package org.mock.tinybank.account.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.account.domain.model.*;
import org.mock.tinybank.account.persistence.Dao.TransactionDao;
import org.mock.tinybank.account.persistence.TransactionPersistenceService;
import org.mock.tinybank.common.UserRecord;
import org.mock.tinybank.user.domain.UserService;

import java.math.BigInteger;
import java.util.List;

import static java.math.BigInteger.valueOf;
import static java.math.BigInteger.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mock.tinybank.account.domain.model.TransactionType.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    private final UserService userService = mock(UserService.class);
    private final TransactionPersistenceService transactionPersistenceService = mock(TransactionPersistenceService.class);
    private AccountService accountService;
    private final String username = "banker_man";
    private final UserRecord user = new UserRecord(username);
    private final String otherUser = "otheruser";

    @BeforeEach
    void beforeEach() {
        accountService = new AccountService(userService, transactionPersistenceService);
    }

    @Test
    void deposit() {
        TransactionDao depositTransaction = new TransactionDao("DEPOSIT_POINT", username, TWO, DEPOSIT);

        when(userService.getUser(username)).thenReturn(user);
        when(transactionPersistenceService.addTransaction(depositTransaction)).thenReturn(depositTransaction);

        AccountAmountRequest deposit = new AccountAmountRequest(username, TWO);
        AccountTransaction actual = accountService.deposit(deposit);
        AccountTransaction expected = new Deposit(TWO);
        assertThat(actual).isEqualTo(expected);
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

        AccountAmountRequest withdrawal = new AccountAmountRequest(username, BigInteger.valueOf(9999));
        assertThatExceptionOfType(InsufficientBalanceException.class)
                .isThrownBy(() -> accountService.withdraw(withdrawal));
    }

    @Test
    void withdraw() {
        TransactionDao withdrawTransaction = new TransactionDao(username, "WITHDRAWAL_POINT", TWO, WITHDRAWAL);

        when(transactionPersistenceService.getTransactions(username)).thenReturn(getMockTransactions());
        when(userService.getUser(username)).thenReturn(user);
        when(transactionPersistenceService.addTransaction(withdrawTransaction)).thenReturn(withdrawTransaction);

        AccountAmountRequest withdrawal = new AccountAmountRequest(username, TWO);
        AccountTransaction actual = accountService.withdraw(withdrawal);
        Withdrawal expected = new Withdrawal(valueOf(-2));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void transfer() {
        TransactionDao transferTransactionDao = new TransactionDao(username, otherUser, TWO, TRANSFER);

        when(transactionPersistenceService.getTransactions(username)).thenReturn(getMockTransactions());
        when(userService.getUser(username)).thenReturn(user);
        when(transactionPersistenceService.addTransaction(transferTransactionDao)).thenReturn(transferTransactionDao);

        TransferRequest transferRequest = new TransferRequest(username, otherUser, TWO);
        AccountTransaction actual = accountService.transfer(transferRequest);
        AccountTransaction expected = new OutgoingTransfer(valueOf(-2), otherUser);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void transfer_InsufficientBalance() {
        when(transactionPersistenceService.getTransactions(username)).thenReturn(getMockTransactions());
        when(userService.getUser(username)).thenReturn(user);

        TransferRequest transferRequest = new TransferRequest(username, otherUser, valueOf(999));
        assertThatExceptionOfType(InsufficientBalanceException.class)
                .isThrownBy(() -> accountService.transfer(transferRequest));
    }

    @Test
    void testGetTransactions() {
        when(userService.getUser(username)).thenReturn(user);
        when(transactionPersistenceService.getTransactions(username)).thenReturn(getMockTransactions());
        List<AccountTransaction> actual = accountService.getTransactions(username);
        assertThat(actual).isEqualTo(getExpectedMappedTransactions());
    }

    private List<TransactionDao> getMockTransactions() {
        return List.of(
                new TransactionDao("DEPOSIT_POINT", username, TEN, DEPOSIT),
                new TransactionDao(username, "WITHDRAWAL_POINT", TWO, WITHDRAWAL),
                new TransactionDao(username, otherUser, TWO, TRANSFER),
                new TransactionDao(otherUser, username, ONE, TRANSFER)
                // 10 - 2 - 2 + 1 = 7
        );
    }

    private List<AccountTransaction> getExpectedMappedTransactions() {
        return List.of(
                new Deposit(TEN),
                new Withdrawal(valueOf(-2)),
                new OutgoingTransfer(valueOf(-2), otherUser),
                new IncomingTransfer(valueOf(1), otherUser)
        );
    }
}