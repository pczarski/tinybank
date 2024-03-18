package org.mock.tinybank.domain;

import org.junit.jupiter.api.Test;
import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.TransactionDto;
import org.mock.tinybank.dto.UnitTransferDto;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {
    @Test
    void mapDepositToTransaction() {
        AccountAmountDto deposit = new AccountAmountDto("user", BigInteger.TEN);
        TransactionDto actual = TransactionMapper.mapDepositToTransaction(deposit);
        TransactionDto expected = new TransactionDto(TransactionMapper.DEPOSIT_POINT, "user", BigInteger.TEN, TransactionType.DEPOSIT);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapTransactionToDeposit() {
        TransactionDto transactionDto = new TransactionDto(TransactionMapper.DEPOSIT_POINT, "user", BigInteger.ONE, TransactionType.DEPOSIT);
        AccountAmountDto actual = TransactionMapper.mapTransactionToDepositWithdrawalDto(transactionDto);
        AccountAmountDto expected = new AccountAmountDto("user", BigInteger.ONE);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapWithdrawalToTransaction() {
        AccountAmountDto withdrawal = new AccountAmountDto("user", BigInteger.TEN);
        TransactionDto actual = TransactionMapper.mapWithdrawalToTransaction(withdrawal);
        TransactionDto expected = new TransactionDto("user", TransactionMapper.WITHDRAWAL_POINT, BigInteger.TEN, TransactionType.WITHDRAWAL);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_Deposit() {
        TransactionDto depositDto = new TransactionDto("source", "user", BigInteger.TEN, TransactionType.DEPOSIT);
        AccountTransaction actual = TransactionMapper.mapFromDtoToAccountTransactionWithdrawalOrDeposit(depositDto, "user");
        AccountTransaction expected = new AccountTransaction(BigInteger.TEN, TransactionType.DEPOSIT);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_Withdrawal() {
        TransactionDto withdrawalDto = new TransactionDto("user", "withdrawal_point", BigInteger.TEN, TransactionType.WITHDRAWAL);
        AccountTransaction actual = TransactionMapper.mapFromDtoToAccountTransactionWithdrawalOrDeposit(withdrawalDto, "user");
        AccountTransaction expected = new AccountTransaction(BigInteger.TEN.negate(), TransactionType.WITHDRAWAL);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_TransferOut() {
        TransactionDto withdrawalDto = new TransactionDto("user", "someone_else", BigInteger.TEN, TransactionType.TRANSFER);
        AccountTransaction actual = TransactionMapper.mapFromDtoToAccountTransactionWithdrawalOrDeposit(withdrawalDto, "user");
        AccountTransaction expected = new AccountTransaction(BigInteger.TEN.negate(), TransactionType.TRANSFER);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_TransferIn() {
        TransactionDto withdrawalDto = new TransactionDto("someone_else", "user", BigInteger.TEN, TransactionType.TRANSFER);
        AccountTransaction actual = TransactionMapper.mapFromDtoToAccountTransactionWithdrawalOrDeposit(withdrawalDto, "user");
        AccountTransaction expected = new AccountTransaction(BigInteger.TEN, TransactionType.TRANSFER);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapTransferToTransaction() {
        UnitTransferDto transferDto = new UnitTransferDto("sender", "recipient", BigInteger.TEN);
        TransactionDto actual = TransactionMapper.mapTransferToTransaction(transferDto);
        TransactionDto expected = new TransactionDto("sender", "recipient", BigInteger.TEN, TransactionType.TRANSFER);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapTransactionToTransfer() {
        TransactionDto inputTransaction = new TransactionDto("sender", "recipient", BigInteger.TWO, TransactionType.TRANSFER);
        UnitTransferDto actual = TransactionMapper.mapTransactionToTransfer(inputTransaction);
        UnitTransferDto expected = new UnitTransferDto("sender", "recipient", BigInteger.TWO);
        assertThat(actual).isEqualTo(expected);
    }
}