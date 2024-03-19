package org.mock.tinybank.api;

import org.mock.tinybank.api.dto.TransactionDto;
import org.mock.tinybank.domain.AccountAmountRequest;
import org.mock.tinybank.domain.AccountService;
import org.mock.tinybank.domain.TransferRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

import static org.mock.tinybank.api.Mapper.toDto;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public TransactionDto deposit(@RequestBody AccountAmountRequest depositRequest) {
        return toDto(accountService.deposit(depositRequest));
    }

    @PostMapping("/withdraw")
    public TransactionDto withdraw(@RequestBody AccountAmountRequest withdrawalRequest) {
        return toDto(accountService.withdraw(withdrawalRequest));
    }

    @GetMapping("/balances/{username}")
    public BigInteger getBalance(@PathVariable String username) {
        return accountService.getBalance(username);
    }

    @PostMapping("/transfer")
    public TransactionDto transfer(@RequestBody TransferRequest transferRequest) {
        return toDto(accountService.transfer(transferRequest));
    }

    @GetMapping("/{username}/transactions")
    public List<TransactionDto> getTransactions(@PathVariable String username) {
        return accountService.getTransactions(username).stream().map(Mapper::toDto).toList();
    }

}
