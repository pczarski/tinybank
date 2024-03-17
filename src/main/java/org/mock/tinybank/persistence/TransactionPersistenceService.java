package org.mock.tinybank.persistence;

import org.mock.tinybank.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionPersistenceService {
    private final List<TransactionDto> transactions;

    public TransactionPersistenceService() {
        transactions = new ArrayList<>();
    }

    public TransactionDto addTransaction(TransactionDto transaction) {
        transactions.add(transaction);
        return transactions.get(transactions.size()-1);
    }

    public List<TransactionDto> getTransactions(String userName) {
        return transactions
                .stream()
                .filter(transactionDto -> transactionDto.fromUser().equals(userName) || transactionDto.toUser().equals(userName))
                .toList();
    }
}
