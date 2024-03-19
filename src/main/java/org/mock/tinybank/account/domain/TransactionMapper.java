package org.mock.tinybank.account.domain;

import org.mock.tinybank.account.domain.model.*;
import org.mock.tinybank.account.persistence.Dao.TransactionDao;

import java.math.BigInteger;

class TransactionMapper {
    static final String DEPOSIT_POINT = "DEPOSIT_POINT";
    static final String WITHDRAWAL_POINT = "WITHDRAWAL_POINT";

    static TransactionDao depositRequestToTransaction(AccountAmountRequest deposit) {
        return new TransactionDao(DEPOSIT_POINT, deposit.username(), deposit.units(), TransactionType.DEPOSIT);
    }

    static TransactionDao withdrawalRequestToTransaction(AccountAmountRequest withdrawal) {
        return new TransactionDao(withdrawal.username(), WITHDRAWAL_POINT, withdrawal.units(), TransactionType.WITHDRAWAL);
    }

    static TransactionDao transferRequestToTransaction(TransferRequest transferDto) {
        return new TransactionDao(transferDto.fromUser(), transferDto.toUser(), transferDto.units(), TransactionType.TRANSFER);
    }

    static AccountTransaction toAccountTransaction(TransactionDao transactionDao, String selectedUsername) {
        boolean isIncomingTransactionType = isIncomingTransactionType(transactionDao, selectedUsername);
        BigInteger netAmount = isIncomingTransactionType ? transactionDao.units() : transactionDao.units().negate();
        if (transactionDao.transactionType() == TransactionType.TRANSFER) {
            String targetUserName = isIncomingTransactionType ? transactionDao.fromUser() : transactionDao.toUser();
            return isIncomingTransactionType ? new IncomingTransfer(netAmount, targetUserName) : new OutgoingTransfer(netAmount, targetUserName);
        }
        return isIncomingTransactionType ? new Deposit(netAmount) : new Withdrawal(netAmount);
    }

    private static boolean isIncomingTransactionType(TransactionDao transactionDao, String username) {
        return transactionDao.toUser().equals(username);
    }
}
