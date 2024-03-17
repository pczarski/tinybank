package org.mock.tinybank.domain;

import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.TransactionDto;

import java.math.BigInteger;

import static org.mock.tinybank.domain.TransactionType.DEPOSIT;
import static org.mock.tinybank.domain.TransactionType.WITHDRAWAL;

class TransactionMapper {
    static final String DEPOSIT_POINT = "DEPOSIT_POINT";
    static final String WITHDRAWAL_POINT = "WITHDRAWAL_POINT";

    static TransactionDto mapDepositToTransaction(AccountAmountDto deposit) {
        return new TransactionDto(DEPOSIT_POINT, deposit.userName(), deposit.units(), DEPOSIT);
    }

    //todo test
    static TransactionDto mapWithdrawalToTransaction(AccountAmountDto withdrawal) {
        return new TransactionDto(withdrawal.userName(), WITHDRAWAL_POINT, withdrawal.units(), WITHDRAWAL);
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
