package org.mock.tinybank.domain;

import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.UnitTransferDto;
import org.mock.tinybank.persistence.TransactionDao;

import java.math.BigInteger;

import static org.mock.tinybank.domain.TransactionType.*;

// could be called a factory
class TransactionMapper {
    static final String DEPOSIT_POINT = "DEPOSIT_POINT";
    static final String WITHDRAWAL_POINT = "WITHDRAWAL_POINT";

    static TransactionDao depositToTransaction(AccountAmountDto deposit) {
        return new TransactionDao(DEPOSIT_POINT, deposit.username(), deposit.units(), DEPOSIT);
    }

    static TransactionDao withdrawalToTransaction(AccountAmountDto withdrawal) {
        return new TransactionDao(withdrawal.username(), WITHDRAWAL_POINT, withdrawal.units(), WITHDRAWAL);
    }

    static TransactionDao toTransaction(UnitTransferDto transferDto) {
        return new TransactionDao(transferDto.fromUser(), transferDto.toUser(), transferDto.units(), TRANSFER);
    }

    static UnitTransferDto toTransfer(TransactionDao transactionDao) {
        return new UnitTransferDto(transactionDao.fromUser(), transactionDao.toUser(), transactionDao.units());
    }

    static AccountAmountDto toDepositWithdrawalDto(TransactionDao transactionDao) {
        String userAccount = transactionDao.transactionType() == DEPOSIT ? transactionDao.toUser() : transactionDao.fromUser();
        return new AccountAmountDto(userAccount, transactionDao.units());
    }

    static AccountTransaction toAccountTransactionWithdrawalOrDeposit(TransactionDao transactionDao, String selectedUsername) {
        boolean isIncomingTransactionType = isIncomingTransactionType(transactionDao, selectedUsername);
        BigInteger netAmount = isIncomingTransactionType ? transactionDao.units() : transactionDao.units().negate();
        if (transactionDao.transactionType() == TRANSFER) {
            String targetUserName = isIncomingTransactionType ? transactionDao.fromUser() : transactionDao.toUser();
            return isIncomingTransactionType ? new AccountTransactionIncomingTransfer(netAmount, TRANSFER, targetUserName) : new AccountTransactionOutgoingTransfer(netAmount, TRANSFER, targetUserName);
        }
        return new AccountTransactionWithdrawalOrDeposit(netAmount, transactionDao.transactionType());
    }

    private static boolean isIncomingTransactionType(TransactionDao transactionDao, String username) {
        return transactionDao.toUser().equals(username);
    }
}
