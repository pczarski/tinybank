package org.mock.tinybank.account.persistence;

import org.mock.tinybank.account.persistence.Dao.TransactionDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionPersistenceService {
    private final List<TransactionDao> transactions;

    public TransactionPersistenceService() {
        transactions = new ArrayList<>();
    }

    public TransactionDao addTransaction(TransactionDao transaction) {
        transactions.add(transaction);
        return transactions.get(transactions.size()-1);
    }

    public List<TransactionDao> getTransactions(String username) {
        return transactions
                .stream()
                .filter(transactionDto -> transactionDto.fromUser().equals(username) || transactionDto.toUser().equals(username))
                .toList();
    }
}
