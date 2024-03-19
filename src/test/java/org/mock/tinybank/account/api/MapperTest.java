package org.mock.tinybank.account.api;

import org.junit.jupiter.api.Test;
import org.mock.tinybank.account.api.dto.TransactionDto;
import org.mock.tinybank.account.domain.model.*;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MapperTest {

    @Test
    public void testToDtoDeposit() {
        AccountTransaction transaction = new Deposit(BigInteger.TEN);

        TransactionDto actual = Mapper.toDto(transaction);

        TransactionDto expected = TransactionDto.builder()
                .netUnits(BigInteger.TEN)
                .transactionType(TransactionType.DEPOSIT)
                .build();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testToDtoWithdrawal() {
        AccountTransaction transaction = new Withdrawal(BigInteger.TEN);

        TransactionDto actual = Mapper.toDto(transaction);

        TransactionDto expected = TransactionDto.builder()
                .netUnits(BigInteger.TEN)
                .transactionType(TransactionType.WITHDRAWAL)
                .build();
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    public void testToDtoIncomingTransfer() {
        String senderName = "John Doe";
        AccountTransaction transaction = new IncomingTransfer(BigInteger.TEN, senderName);

        TransactionDto actual = Mapper.toDto(transaction);

        TransactionDto expected = TransactionDto.builder()
                .netUnits(BigInteger.TEN)
                .transactionType(TransactionType.TRANSFER)
                .sender(senderName)
                .build();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testToDtoOutgoingTransfer() {
        String receiverName = "Jane Doe";
        AccountTransaction transaction = new OutgoingTransfer(BigInteger.TEN, receiverName);

        TransactionDto actual = Mapper.toDto(transaction);

        TransactionDto expected = TransactionDto.builder()
                .netUnits(BigInteger.TEN)
                .transactionType(TransactionType.TRANSFER)
                .receiver(receiverName)
                .build();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testToDtoUnsupportedType() {
        AccountTransaction unSupportedAccountTransaction = new MockUnSupportedAccountTransaction(BigInteger.ONE);
        assertThatExceptionOfType(UnsupportedAccountTransactionException.class).isThrownBy(() -> Mapper.toDto(unSupportedAccountTransaction));
    }

    private record MockUnSupportedAccountTransaction(BigInteger netUnits) implements AccountTransaction {
    }
}