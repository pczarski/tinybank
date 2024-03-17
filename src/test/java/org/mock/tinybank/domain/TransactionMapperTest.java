package org.mock.tinybank.domain;

import org.junit.jupiter.api.Test;
import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.TransactionDto;

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
        AccountTransaction actual = TransactionMapper.mapFromDtoToAccountTransaction(depositDto);
        AccountTransaction expected = new AccountTransaction(BigInteger.TEN, TransactionType.DEPOSIT);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapFromDtoToAccountTransaction_Withdrawal() {
        TransactionDto withdrawalDto = new TransactionDto("user", "destination", BigInteger.TEN, TransactionType.WITHDRAWAL);
        AccountTransaction actual = TransactionMapper.mapFromDtoToAccountTransaction(withdrawalDto);
        AccountTransaction expected = new AccountTransaction(BigInteger.TEN.negate(), TransactionType.WITHDRAWAL);
        assertThat(actual).isEqualTo(expected);
    }
}