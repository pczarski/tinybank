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
}