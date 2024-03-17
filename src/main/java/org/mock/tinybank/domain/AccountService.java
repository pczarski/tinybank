package org.mock.tinybank.domain;

import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.TransactionDto;
import org.mock.tinybank.persistence.TransactionPersistenceService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static org.mock.tinybank.domain.TransactionMapper.mapDepositToTransaction;
import static org.mock.tinybank.domain.TransactionMapper.mapTransactionToDepositWithdrawalDto;

@Service
public class AccountService {

    private final UserService userService;

    private final TransactionPersistenceService transactionPersistenceService;

    public AccountService(UserService userService, TransactionPersistenceService transactionPersistenceService) {
        this.userService = userService;
        this.transactionPersistenceService = transactionPersistenceService;
    }

    public AccountAmountDto deposit(AccountAmountDto deposit) {
        userService.getUser(deposit.userName());
        TransactionDto transactionDto = transactionPersistenceService.addTransaction(mapDepositToTransaction(deposit));
        return mapTransactionToDepositWithdrawalDto(transactionDto);
    }

    //todo test
    public BigInteger getBalance(String userName) {
        userService.getUser(userName);
        List<TransactionDto> transactions = transactionPersistenceService.getTransactions(userName);
        User user = new User(transactions.stream().map(TransactionMapper::mapFromDtoToAccountTransaction).toList());
        return user.getBalance();
    }
}
