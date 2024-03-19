package org.mock.tinybank.domain;

import org.mock.tinybank.persistence.TransactionDao;

import java.math.BigInteger;

import static org.mock.tinybank.domain.TransactionType.*;

class TransactionMapper {
    static final String DEPOSIT_POINT = "DEPOSIT_POINT";
    static final String WITHDRAWAL_POINT = "WITHDRAWAL_POINT";

    static TransactionDao depositToTransaction(AccountAmountRequest deposit) {
        return new TransactionDao(DEPOSIT_POINT, deposit.username(), deposit.units(), DEPOSIT);
    }

    static TransactionDao withdrawalToTransaction(AccountAmountRequest withdrawal) {
        return new TransactionDao(withdrawal.username(), WITHDRAWAL_POINT, withdrawal.units(), WITHDRAWAL);
    }

    static TransactionDao toTransaction(UnitTransferRecord transferDto) {
        return new TransactionDao(transferDto.fromUser(), transferDto.toUser(), transferDto.units(), TRANSFER);
    }

    static UnitTransferRecord toTransfer(TransactionDao transactionDao) {
        return new UnitTransferRecord(transactionDao.fromUser(), transactionDao.toUser(), transactionDao.units());
    }

    static AccountAmountRequest toAccountAmountRecord(TransactionDao transactionDao) {
        String userAccount = transactionDao.transactionType() == DEPOSIT ? transactionDao.toUser() : transactionDao.fromUser();
        return new AccountAmountRequest(userAccount, transactionDao.units());
    }

    static AccountTransaction toAccountTransaction(TransactionDao transactionDao, String selectedUsername) {
        boolean isIncomingTransactionType = isIncomingTransactionType(transactionDao, selectedUsername);
        BigInteger netAmount = isIncomingTransactionType ? transactionDao.units() : transactionDao.units().negate();
        if (transactionDao.transactionType() == TRANSFER) {
            String targetUserName = isIncomingTransactionType ? transactionDao.fromUser() : transactionDao.toUser();
            return isIncomingTransactionType ? new IncomingTransfer(netAmount, TRANSFER, targetUserName) : new OutgoingTransfer(netAmount, TRANSFER, targetUserName);
        }
        return isIncomingTransactionType ? new Deposit(netAmount, DEPOSIT) : new Withdrawal(netAmount, WITHDRAWAL);
    }

    private static boolean isIncomingTransactionType(TransactionDao transactionDao, String username) {
        return transactionDao.toUser().equals(username);
    }
}
