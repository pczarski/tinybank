package org.mock.tinybank.domain;

import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.TransactionDto;
import org.mock.tinybank.dto.UnitTransferDto;

import java.math.BigInteger;

import static org.mock.tinybank.domain.TransactionType.*;

// could be called a factory
class TransactionMapper {
    static final String DEPOSIT_POINT = "DEPOSIT_POINT";
    static final String WITHDRAWAL_POINT = "WITHDRAWAL_POINT";

    static TransactionDto mapDepositToTransaction(AccountAmountDto deposit) {
        return new TransactionDto(DEPOSIT_POINT, deposit.username(), deposit.units(), DEPOSIT);
    }

    static TransactionDto mapWithdrawalToTransaction(AccountAmountDto withdrawal) {
        return new TransactionDto(withdrawal.username(), WITHDRAWAL_POINT, withdrawal.units(), WITHDRAWAL);
    }

    static TransactionDto mapTransferToTransaction(UnitTransferDto transferDto) {
        return new TransactionDto(transferDto.fromUser(), transferDto.toUser(), transferDto.units(), TRANSFER);
    }

    static UnitTransferDto mapTransactionToTransfer(TransactionDto transactionDto) {
        return new UnitTransferDto(transactionDto.fromUser(), transactionDto.toUser(), transactionDto.units());
    }
    static AccountAmountDto mapTransactionToDepositWithdrawalDto(TransactionDto transactionDto) {
        String userAccount = transactionDto.transactionType() == DEPOSIT ? transactionDto.toUser() : transactionDto.fromUser();
        return new AccountAmountDto(userAccount, transactionDto.units());
    }

    static AccountTransaction mapFromDtoToAccountTransactionWithdrawalOrDeposit(TransactionDto transactionDto, String selectedUsername) {
        boolean isIncomingTransactionType = isIncomingTransactionType(transactionDto, selectedUsername);
        BigInteger netAmount = isIncomingTransactionType ? transactionDto.units() : transactionDto.units().negate();
        if (transactionDto.transactionType() == TRANSFER) {
            String targetUserName = isIncomingTransactionType ? transactionDto.fromUser() : transactionDto.toUser();
            return isIncomingTransactionType ? new AccountTransactionIncomingTransfer(netAmount, targetUserName) : new AccountTransactionOutgoingTransfer(netAmount, targetUserName);
        }
        return new AccountTransactionWithdrawalOrDeposit(netAmount, transactionDto.transactionType());
    }

    private static boolean isIncomingTransactionType(TransactionDto transactionDto, String username) {
        return transactionDto.toUser().equals(username);
    }
}
