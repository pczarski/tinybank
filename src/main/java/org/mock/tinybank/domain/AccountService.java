package org.mock.tinybank.domain;

import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.TransactionDto;
import org.mock.tinybank.dto.UnitTransferDto;
import org.mock.tinybank.dto.UserDto;
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

    public AccountAmountDto deposit(AccountAmountDto deposit) {
        userService.getUser(deposit.username());
        TransactionDto transactionDto = transactionPersistenceService.addTransaction(mapDepositToTransaction(deposit));
        return mapTransactionToDepositWithdrawalDto(transactionDto);
    }

    //todo test
    public BigInteger getBalance(String username) {
        UserDto userDto = userService.getUser(username);
        List<TransactionDto> transactions = transactionPersistenceService.getTransactions(username);
        User user = new User(transactions.stream().map((transaction) -> TransactionMapper.mapFromDtoToAccountTransactionWithdrawalOrDeposit(transaction, userDto.username())).toList());
        return user.getBalance();
    }

    //todo test
    public AccountAmountDto withdraw(AccountAmountDto withdrawal) {
        BigInteger balance = getBalance(withdrawal.username());
        if (isPositive(balance.subtract(withdrawal.units()))) {
            TransactionDto transactionDto = mapWithdrawalToTransaction(withdrawal);
            TransactionDto withdrawalTransaction = transactionPersistenceService.addTransaction(transactionDto);
            return mapTransactionToDepositWithdrawalDto(withdrawalTransaction);
        }
        throw new InsufficientBalanceException();
    }

    //todo test
    public UnitTransferDto transfer(UnitTransferDto transactionDto) {
        userService.getUser(transactionDto.toUser());
        BigInteger senderBalance = getBalance(transactionDto.fromUser());
        if (isPositive(senderBalance.subtract(transactionDto.units()))) {
            TransactionDto transferTransactionDto = transactionPersistenceService.addTransaction(mapTransferToTransaction(transactionDto));
            return mapTransactionToTransfer(transferTransactionDto);
        }
        throw new InsufficientBalanceException();
    }

    private boolean isPositive(BigInteger value) {
        return value.compareTo(BigInteger.ZERO) > 0;
    }
}
