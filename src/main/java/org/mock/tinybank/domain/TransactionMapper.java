package org.mock.tinybank.domain;

import org.mock.tinybank.dto.DepositWithdrawDto;
import org.mock.tinybank.dto.TransactionDto;

import static org.mock.tinybank.domain.TransactionType.DEPOSIT;

class TransactionMapper {
    static final String DEPOSIT_POINT = "DEPOSIT_POINT";

    static TransactionDto mapDepositToTransaction(DepositWithdrawDto depositWithdrawDto) {
        return new TransactionDto(DEPOSIT_POINT, depositWithdrawDto.userName(), depositWithdrawDto.units(), DEPOSIT);
    }

    static DepositWithdrawDto mapTransactionToDepositWithdrawalDto(TransactionDto transactionDto) {
        String userAccount = transactionDto.transactionType() == DEPOSIT ? transactionDto.toUser() : transactionDto.fromUser();
        return new DepositWithdrawDto(userAccount, transactionDto.units());
    }
}
