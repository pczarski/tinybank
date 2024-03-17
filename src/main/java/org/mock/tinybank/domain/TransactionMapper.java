package org.mock.tinybank.domain;

import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.TransactionDto;

import java.math.BigInteger;

import static org.mock.tinybank.domain.TransactionType.DEPOSIT;

class TransactionMapper {
    static final String DEPOSIT_POINT = "DEPOSIT_POINT";

    static TransactionDto mapDepositToTransaction(AccountAmountDto accountAmountDto) {
        return new TransactionDto(DEPOSIT_POINT, accountAmountDto.userName(), accountAmountDto.units(), DEPOSIT);
    }

    static AccountAmountDto mapTransactionToDepositWithdrawalDto(TransactionDto transactionDto) {
        String userAccount = transactionDto.transactionType() == DEPOSIT ? transactionDto.toUser() : transactionDto.fromUser();
        return new AccountAmountDto(userAccount, transactionDto.units());
    }

    // todo test
    static AccountTransaction mapFromDtoToAccountTransaction(TransactionDto transactionDto) {
        BigInteger netAmount = isNetPositiveTransaction(transactionDto) ? transactionDto.units() : transactionDto.units().negate();
        return new AccountTransaction(netAmount, transactionDto.transactionType());
    }

    private static boolean isNetPositiveTransaction(TransactionDto transactionDto) {
        return transactionDto.transactionType() == DEPOSIT;
    }
}
