package org.mock.tinybank.account.domain;

import org.junit.jupiter.api.Test;
import org.mock.tinybank.account.domain.model.*;
import org.mock.tinybank.account.persistence.Dao.TransactionDao;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {
    @Test
    void mapDepositToTransaction() {
        AccountAmountRequest deposit = new AccountAmountRequest("user", BigInteger.TEN);
        TransactionDao actual = TransactionMapper.depositRequestToTransaction(deposit);
        TransactionDao expected = new TransactionDao(TransactionMapper.DEPOSIT_POINT, "user", BigInteger.TEN, TransactionType.DEPOSIT);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapWithdrawalToTransaction() {
        AccountAmountRequest withdrawal = new AccountAmountRequest("user", BigInteger.TEN);
        TransactionDao actual = TransactionMapper.withdrawalRequestToTransaction(withdrawal);
        TransactionDao expected = new TransactionDao("user", TransactionMapper.WITHDRAWAL_POINT, BigInteger.TEN, TransactionType.WITHDRAWAL);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_Deposit() {
        TransactionDao depositDto = new TransactionDao("source", "user", BigInteger.TEN, TransactionType.DEPOSIT);
        AccountTransaction actual = TransactionMapper.toAccountTransaction(depositDto, "user");
        Deposit expected = new Deposit(BigInteger.TEN);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_Withdrawal() {
        TransactionDao withdrawalDto = new TransactionDao("user", "withdrawal_point", BigInteger.TEN, TransactionType.WITHDRAWAL);
        AccountTransaction actual = TransactionMapper.toAccountTransaction(withdrawalDto, "user");
        Withdrawal expected = new Withdrawal(BigInteger.TEN.negate());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_TransferOut() {
        TransactionDao withdrawalDto = new TransactionDao("user", "someone_else", BigInteger.TEN, TransactionType.TRANSFER);
        AccountTransaction actual = TransactionMapper.toAccountTransaction(withdrawalDto, "user");
        OutgoingTransfer expected = new OutgoingTransfer(BigInteger.TEN.negate(), "someone_else");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_TransferIn() {
        TransactionDao withdrawalDto = new TransactionDao("someone_else", "user", BigInteger.TEN, TransactionType.TRANSFER);
        AccountTransaction actual = TransactionMapper.toAccountTransaction(withdrawalDto, "user");
        IncomingTransfer expected = new IncomingTransfer(BigInteger.TEN, "someone_else");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapTransferToTransaction() {
        TransferRequest transferDto = new TransferRequest("sender", "recipient", BigInteger.TEN);
        TransactionDao actual = TransactionMapper.transferRequestToTransaction(transferDto);
        TransactionDao expected = new TransactionDao("sender", "recipient", BigInteger.TEN, TransactionType.TRANSFER);
        assertThat(actual).isEqualTo(expected);
    }

}