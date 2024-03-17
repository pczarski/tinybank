package org.mock.tinybank.api;

import org.mock.tinybank.domain.AccountService;
import org.mock.tinybank.dto.AccountAmountDto;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public AccountAmountDto deposit(@RequestBody AccountAmountDto deposit) {
        return accountService.deposit(deposit);
    }

    @PostMapping("/withdraw")
    public AccountAmountDto withdraw(@RequestBody AccountAmountDto withdrawal) {
        return accountService.withdraw(withdrawal);
    }

    @GetMapping("/balances/{userName}")
    public BigInteger getBalance(@PathVariable String userName) {
        return accountService.getBalance(userName);
    }
}
