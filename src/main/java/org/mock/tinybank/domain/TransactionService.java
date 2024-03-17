package org.mock.tinybank.domain;

import org.mock.tinybank.dto.DepositWithdrawDto;
import org.mock.tinybank.dto.TransactionDto;
import org.mock.tinybank.persistence.TransactionPersistenceService;
import org.springframework.stereotype.Service;

import static org.mock.tinybank.domain.TransactionMapper.mapDepositToTransaction;
import static org.mock.tinybank.domain.TransactionMapper.mapTransactionToDepositWithdrawalDto;

@Service
public class TransactionService {

    private final UserService userService;

    private final TransactionPersistenceService transactionPersistenceService;

    public TransactionService(UserService userService, TransactionPersistenceService transactionPersistenceService) {
        this.userService = userService;
        this.transactionPersistenceService = transactionPersistenceService;
    }

    public DepositWithdrawDto deposit(DepositWithdrawDto deposit) {
        userService.getUser(deposit.userName());
        TransactionDto transactionDto = transactionPersistenceService.addTransaction(mapDepositToTransaction(deposit));
        return mapTransactionToDepositWithdrawalDto(transactionDto);
    }
}
