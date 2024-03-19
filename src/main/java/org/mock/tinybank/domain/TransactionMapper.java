package org.mock.tinybank.domain;

import org.mock.tinybank.persistence.TransactionDao;

import java.math.BigInteger;

import static org.mock.tinybank.domain.TransactionType.*;

class TransactionMapper {
    static final String DEPOSIT_POINT = "DEPOSIT_POINT";
    static final String WITHDRAWAL_POINT = "WITHDRAWAL_POINT";

    static TransactionDao depositRequestToTransaction(AccountAmountRequest deposit) {
        return new TransactionDao(DEPOSIT_POINT, deposit.username(), deposit.units(), DEPOSIT);
    }

    static TransactionDao withdrawalRequestToTransaction(AccountAmountRequest withdrawal) {
        return new TransactionDao(withdrawal.username(), WITHDRAWAL_POINT, withdrawal.units(), WITHDRAWAL);
    }

    static TransactionDao transferRequestToTransaction(TransferRequest transferDto) {
        return new TransactionDao(transferDto.fromUser(), transferDto.toUser(), transferDto.units(), TRANSFER);
    }

    static AccountTransaction toAccountTransaction(TransactionDao transactionDao, String selectedUsername) {
        boolean isIncomingTransactionType = isIncomingTransactionType(transactionDao, selectedUsername);
        BigInteger netAmount = isIncomingTransactionType ? transactionDao.units() : transactionDao.units().negate();
        if (transactionDao.transactionType() == TRANSFER) {
            String targetUserName = isIncomingTransactionType ? transactionDao.fromUser() : transactionDao.toUser();
            return isIncomingTransactionType ? new IncomingTransfer(netAmount, targetUserName) : new OutgoingTransfer(netAmount, targetUserName);
        }
        return isIncomingTransactionType ? new Deposit(netAmount) : new Withdrawal(netAmount);
    }

    private static boolean isIncomingTransactionType(TransactionDao transactionDao, String username) {
        return transactionDao.toUser().equals(username);
    }
}
