package com.banking.Controller;

import com.banking.Dtos.DepositRequest;
import com.banking.Dtos.TransferRequest;
import com.banking.Dtos.WithdrawRequest;
import com.banking.Entity.Transaction;
import com.banking.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit/{accountNumber}")
    public String deposit(
            @PathVariable String accountNumber,
            @RequestBody DepositRequest request
    ) {
        return transactionService.deposit(accountNumber, request);
    }

    @PostMapping("/withdraw/{accountNumber}")
    public String withdraw(
            @PathVariable String accountNumber,
            @RequestBody WithdrawRequest request
    ) {
        return transactionService.withdraw(accountNumber, request);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        return transactionService.transfer(request);
    }

    @GetMapping("/history/{accountNumber}")
    public Page<Transaction> getHistory(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return transactionService.getTransactionHistory(accountNumber, page, size);
    }
}
