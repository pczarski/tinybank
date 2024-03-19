package org.mock.tinybank.account.api;

import org.mock.tinybank.account.api.dto.TransactionDto;
import org.mock.tinybank.account.domain.AccountService;
import org.mock.tinybank.account.domain.model.AccountAmountRequest;
import org.mock.tinybank.account.domain.model.TransferRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public TransactionDto deposit(@RequestBody AccountAmountRequest depositRequest) {
        return Mapper.toDto(accountService.deposit(depositRequest));
    }

    @PostMapping("/withdraw")
    public TransactionDto withdraw(@RequestBody AccountAmountRequest withdrawalRequest) {
        return Mapper.toDto(accountService.withdraw(withdrawalRequest));
    }

    @GetMapping("/balances/{username}")
    public BigInteger getBalance(@PathVariable String username) {
        return accountService.getBalance(username);
    }

    @PostMapping("/transfer")
    public TransactionDto transfer(@RequestBody TransferRequest transferRequest) {
        return Mapper.toDto(accountService.transfer(transferRequest));
    }

    @GetMapping("/{username}/transactions")
    public List<TransactionDto> getTransactions(@PathVariable String username) {
        return accountService.getTransactions(username).stream().map(Mapper::toDto).toList();
    }

}
