package org.mock.tinybank.api;

import org.mock.tinybank.domain.AccountAmountRecord;
import org.mock.tinybank.domain.AccountService;
import org.mock.tinybank.domain.AccountTransaction;
import org.mock.tinybank.domain.UnitTransferRecord;
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
    public AccountAmountRecord deposit(@RequestBody AccountAmountRecord deposit) {
        return accountService.deposit(deposit);
    }

    @PostMapping("/withdraw")
    public AccountAmountRecord withdraw(@RequestBody AccountAmountRecord withdrawal) {
        return accountService.withdraw(withdrawal);
    }

    @GetMapping("/balances/{username}")
    public BigInteger getBalance(@PathVariable String username) {
        return accountService.getBalance(username);
    }

    @PostMapping("/transfer")
    public UnitTransferRecord transfer(@RequestBody UnitTransferRecord transferDto) {
        return accountService.transfer(transferDto);
    }

    @GetMapping("/{username}/transactions")
    public List<AccountTransaction> getTransactions(@PathVariable String username) {
        return accountService.getTransactions(username);
    }

}
