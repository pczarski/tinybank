package org.mock.tinybank.domain;

import org.mock.tinybank.persistence.TransactionDao;
import org.mock.tinybank.persistence.TransactionPersistenceService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static org.mock.tinybank.domain.TransactionMapper.*;

@Service
public class AccountService {

    private final UserService userService;

    private final TransactionPersistenceService transactionPersistenceService;

    public AccountService(UserService userService, TransactionPersistenceService transactionPersistenceService) {
        this.userService = userService;
        this.transactionPersistenceService = transactionPersistenceService;
    }

    public AccountAmountRecord deposit(AccountAmountRecord deposit) {
        userService.getUser(deposit.username());
        TransactionDao transactionDao = transactionPersistenceService.addTransaction(depositToTransaction(deposit));
        return toDepositWithdrawalDto(transactionDao);
    }

    public BigInteger getBalance(String username) {
        UserRecord userRecord = userService.getUser(username);
        User user = constructUser(userRecord.username());
        return user.getBalance();
    }

    public AccountAmountRecord withdraw(AccountAmountRecord withdrawal) {
        BigInteger balance = getBalance(withdrawal.username());
        if (isPositive(balance.subtract(withdrawal.units()))) {
            TransactionDao transactionDao = withdrawalToTransaction(withdrawal);
            TransactionDao withdrawalTransaction = transactionPersistenceService.addTransaction(transactionDao);
            return toDepositWithdrawalDto(withdrawalTransaction);
        }
        throw new InsufficientBalanceException();
    }

    public UnitTransferRecord transfer(UnitTransferRecord transactionDto) {
        userService.getUser(transactionDto.toUser()); //this could be a filter
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
        return new User(transactions.stream().map((transaction) -> TransactionMapper.toAccountTransactionWithdrawalOrDeposit(transaction, username)).toList());
    }

    private boolean isPositive(BigInteger value) {
        return value.compareTo(BigInteger.ZERO) > 0;
    }
}
