package org.mock.tinybank.domain;

import org.junit.jupiter.api.Test;
import org.mock.tinybank.persistence.TransactionDao;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {
    @Test
    void mapDepositToTransaction() {
        AccountAmountRecord deposit = new AccountAmountRecord("user", BigInteger.TEN);
        TransactionDao actual = TransactionMapper.depositToTransaction(deposit);
        TransactionDao expected = new TransactionDao(TransactionMapper.DEPOSIT_POINT, "user", BigInteger.TEN, TransactionType.DEPOSIT);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapTransactionToDeposit() {
        TransactionDao transactionDao = new TransactionDao(TransactionMapper.DEPOSIT_POINT, "user", BigInteger.ONE, TransactionType.DEPOSIT);
        AccountAmountRecord actual = TransactionMapper.toDepositWithdrawalDto(transactionDao);
        AccountAmountRecord expected = new AccountAmountRecord("user", BigInteger.ONE);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapWithdrawalToTransaction() {
        AccountAmountRecord withdrawal = new AccountAmountRecord("user", BigInteger.TEN);
        TransactionDao actual = TransactionMapper.withdrawalToTransaction(withdrawal);
        TransactionDao expected = new TransactionDao("user", TransactionMapper.WITHDRAWAL_POINT, BigInteger.TEN, TransactionType.WITHDRAWAL);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_Deposit() {
        TransactionDao depositDto = new TransactionDao("source", "user", BigInteger.TEN, TransactionType.DEPOSIT);
        AccountTransaction actual = TransactionMapper.toAccountTransactionWithdrawalOrDeposit(depositDto, "user");
        Deposit expected = new Deposit(BigInteger.TEN, TransactionType.DEPOSIT);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_Withdrawal() {
        TransactionDao withdrawalDto = new TransactionDao("user", "withdrawal_point", BigInteger.TEN, TransactionType.WITHDRAWAL);
        AccountTransaction actual = TransactionMapper.toAccountTransactionWithdrawalOrDeposit(withdrawalDto, "user");
        Withdrawal expected = new Withdrawal(BigInteger.TEN.negate(), TransactionType.WITHDRAWAL);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_TransferOut() {
        TransactionDao withdrawalDto = new TransactionDao("user", "someone_else", BigInteger.TEN, TransactionType.TRANSFER);
        AccountTransaction actual = TransactionMapper.toAccountTransactionWithdrawalOrDeposit(withdrawalDto, "user");
        OutgoingTransfer expected = new OutgoingTransfer(BigInteger.TEN.negate(), TransactionType.TRANSFER, "someone_else");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_TransferIn() {
        TransactionDao withdrawalDto = new TransactionDao("someone_else", "user", BigInteger.TEN, TransactionType.TRANSFER);
        AccountTransaction actual = TransactionMapper.toAccountTransactionWithdrawalOrDeposit(withdrawalDto, "user");
        IncomingTransfer expected = new IncomingTransfer(BigInteger.TEN, TransactionType.TRANSFER, "someone_else");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapTransferToTransaction() {
        UnitTransferRecord transferDto = new UnitTransferRecord("sender", "recipient", BigInteger.TEN);
        TransactionDao actual = TransactionMapper.toTransaction(transferDto);
        TransactionDao expected = new TransactionDao("sender", "recipient", BigInteger.TEN, TransactionType.TRANSFER);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapTransactionToTransfer() {
        TransactionDao inputTransaction = new TransactionDao("sender", "recipient", BigInteger.TWO, TransactionType.TRANSFER);
        UnitTransferRecord actual = TransactionMapper.toTransfer(inputTransaction);
        UnitTransferRecord expected = new UnitTransferRecord("sender", "recipient", BigInteger.TWO);
        assertThat(actual).isEqualTo(expected);
    }
}