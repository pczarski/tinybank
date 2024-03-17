package org.mock.tinybank.api;

import org.mock.tinybank.domain.TransactionService;
import org.mock.tinybank.dto.DepositWithdrawDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public DepositWithdrawDto deposit(@RequestBody DepositWithdrawDto deposit) {
        return transactionService.deposit(deposit);
    }
}
