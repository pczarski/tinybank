package org.mock.tinybank.account.domain;

import lombok.RequiredArgsConstructor;
import org.mock.tinybank.account.domain.model.*;
import org.mock.tinybank.account.persistence.Dao.TransactionDao;
import org.mock.tinybank.account.persistence.TransactionPersistenceService;
import org.mock.tinybank.user.domain.UserRecord;
import org.mock.tinybank.user.domain.UserService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static org.mock.tinybank.account.domain.TransactionMapper.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserService userService;

    private final TransactionPersistenceService transactionPersistenceService;

    public BigInteger getBalance(String username) {
        UserRecord userRecord = userService.getUser(username);
        User user = constructUser(userRecord.username());
        return user.getBalance();
    }

    public AccountTransaction deposit(AccountAmountRequest deposit) {
        UserRecord user = userService.getUser(deposit.username());
        TransactionDao transactionDao = transactionPersistenceService.addTransaction(depositRequestToTransaction(deposit));
        return toAccountTransaction(transactionDao, user.username());
    }


    public AccountTransaction withdraw(AccountAmountRequest withdrawal) {
        UserRecord user = userService.getUser(withdrawal.username());
        BigInteger balance = getBalance(withdrawal.username());
        if (isPositive(balance.subtract(withdrawal.units()))) {
            TransactionDao transactionDao = withdrawalRequestToTransaction(withdrawal);
            TransactionDao withdrawalTransaction = transactionPersistenceService.addTransaction(transactionDao);
            return toAccountTransaction(withdrawalTransaction, user.username());
        }
        throw new InsufficientBalanceException();
    }

    public AccountTransaction transfer(TransferRequest transactionRequest) {
        UserRecord sender = userService.getUser(transactionRequest.fromUser());
        BigInteger senderBalance = getBalance(transactionRequest.fromUser());
        if (isPositive(senderBalance.subtract(transactionRequest.units()))) {
            TransactionDao transferTransactionDao = transactionPersistenceService.addTransaction(transferRequestToTransaction(transactionRequest));
            return toAccountTransaction(transferTransactionDao, sender.username());
        }
        throw new InsufficientBalanceException();
    }

    public List<AccountTransaction> getTransactions(String username) {
        userService.getUser(username);
        return constructUser(username).transactions();
    }

    private User constructUser(String username) {
        List<TransactionDao> transactions = transactionPersistenceService.getTransactions(username);
        return new User(transactions.stream().map((transaction) -> TransactionMapper.toAccountTransaction(transaction, username)).toList());
    }

    private boolean isPositive(BigInteger value) {
        return value.compareTo(BigInteger.ZERO) > 0;
    }
}
