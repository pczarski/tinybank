package org.mock.tinybank.api;

import org.mock.tinybank.domain.AccountService;
import org.mock.tinybank.domain.AccountTransaction;
import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.UnitTransferDto;
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
    public AccountAmountDto deposit(@RequestBody AccountAmountDto deposit) {
        return accountService.deposit(deposit);
    }

    @PostMapping("/withdraw")
    public AccountAmountDto withdraw(@RequestBody AccountAmountDto withdrawal) {
        return accountService.withdraw(withdrawal);
    }

    @GetMapping("/balances/{username}")
    public BigInteger getBalance(@PathVariable String username) {
        return accountService.getBalance(username);
    }

    @PostMapping("/transfer")
    public UnitTransferDto transfer(@RequestBody UnitTransferDto transferDto) {
        return accountService.transfer(transferDto);
    }

    // todo test
    @GetMapping("/{username}/transactions")
    public List<AccountTransaction> getTransactions(@PathVariable String username) {
        return accountService.getTransactions(username);
    }

}
