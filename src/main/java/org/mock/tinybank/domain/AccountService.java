package org.mock.tinybank.domain;

import lombok.RequiredArgsConstructor;
import org.mock.tinybank.persistence.TransactionDao;
import org.mock.tinybank.persistence.TransactionPersistenceService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static org.mock.tinybank.domain.TransactionMapper.*;

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
        TransactionDao transactionDao = transactionPersistenceService.addTransaction(depositToTransaction(deposit));
        return toAccountTransaction(transactionDao, user.username());
    }


    public AccountTransaction withdraw(AccountAmountRequest withdrawal) {
        UserRecord user = userService.getUser(withdrawal.username());
        BigInteger balance = getBalance(withdrawal.username());
        if (isPositive(balance.subtract(withdrawal.units()))) {
            TransactionDao transactionDao = withdrawalToTransaction(withdrawal);
            TransactionDao withdrawalTransaction = transactionPersistenceService.addTransaction(transactionDao);
            return toAccountTransaction(withdrawalTransaction, user.username());
        }
        throw new InsufficientBalanceException();
    }

    public UnitTransferRecord transfer(UnitTransferRecord transactionDto) {
        userService.getUser(transactionDto.toUser());
        BigInteger senderBalance = getBalance(transactionDto.fromUser());
        if (isPositive(senderBalance.subtract(transactionDto.units()))) {
            TransactionDao transferTransactionDao = transactionPersistenceService.addTransaction(toTransaction(transactionDto));
            return toTransfer(transferTransactionDao);
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
