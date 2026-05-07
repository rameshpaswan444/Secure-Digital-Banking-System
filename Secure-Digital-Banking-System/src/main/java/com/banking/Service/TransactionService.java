package com.banking.Service;
import com.banking.Dtos.DepositRequest;
import com.banking.Dtos.TransferRequest;
import com.banking.Dtos.WithdrawRequest;
import com.banking.Entity.Account;
import com.banking.Entity.Transaction;
import com.banking.Exception.ResourceNotFoundException;
import com.banking.Repository.AccountRepository;
import com.banking.Repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final EmailService emailService;

    // 💰 DEPOSIT MONEY
    public String deposit(String accountNumber, DepositRequest request) {

        Account account = accountRepository.findAll()
                .stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // ➕ update balance
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        // 🧾 save transaction
        Transaction transaction = Transaction.builder()
                .type("DEPOSIT")
                .amount(request.getAmount())
                .timestamp(LocalDateTime.now())
                .accountNumber(accountNumber)
                .description("Money deposited")
                .build();

        transactionRepository.save(transaction);

        emailService.sendDepositEmail(
                account.getUser().getEmail(),
                account.getAccountNumber(),
                request.getAmount().toString(),
                account.getBalance().toString()
        );

        return "Deposit successful. New balance: " + account.getBalance();
    }

    public String withdraw(String accountNumber, WithdrawRequest request) {

        Account account = accountRepository.findAll()
                .stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // 🚫 VALIDATION (VERY IMPORTANT)
        if (request.getAmount().compareTo(account.getBalance()) > 0) {
            throw new ResourceNotFoundException("Insufficient balance");
        }

        // ➖ subtract balance
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        // 🧾 save transaction
        Transaction transaction = Transaction.builder()
                .type("WITHDRAW")
                .amount(request.getAmount())
                .timestamp(java.time.LocalDateTime.now())
                .accountNumber(accountNumber)
                .description("Money withdrawn")
                .build();

        transactionRepository.save(transaction);

        emailService.sendWithdrawEmail(
                account.getUser().getEmail(),
                account.getAccountNumber(),
                request.getAmount().toString(),
                account.getBalance().toString()
        );

        return "Withdraw successful. Remaining balance: " + account.getBalance();
    }



    @Transactional
    public String transfer(TransferRequest request) {

        // 🔍 Get sender
        Account sender = accountRepository
                .findByAccountNumber(request.getFromAccount())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        // 🔍 Get receiver
        Account receiver = accountRepository
                .findByAccountNumber(request.getToAccount())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        // 🚫 Prevent same account transfer
        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        // 🚫 Check balance
        if (request.getAmount().compareTo(sender.getBalance()) > 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // ➖ Deduct from sender
        sender.setBalance(sender.getBalance().subtract(request.getAmount()));

        // ➕ Add to receiver
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // 🧾 SAVE TRANSACTION (SENDER)
        Transaction senderTx = Transaction.builder()
                .type("TRANSFER_OUT")
                .amount(request.getAmount())
                .timestamp(java.time.LocalDateTime.now())
                .accountNumber(sender.getAccountNumber())
                .description("Transferred to " + receiver.getAccountNumber())
                .build();

        // 🧾 SAVE TRANSACTION (RECEIVER)
        Transaction receiverTx = Transaction.builder()
                .type("TRANSFER_IN")
                .amount(request.getAmount())
                .timestamp(java.time.LocalDateTime.now())
                .accountNumber(receiver.getAccountNumber())
                .description("Received from " + sender.getAccountNumber())
                .build();

        transactionRepository.save(senderTx);
        transactionRepository.save(receiverTx);

        emailService.sendTransferEmail(
                sender.getUser().getEmail(),
                sender.getAccountNumber(),
                receiver.getAccountNumber(),
                request.getAmount().toString(),
                sender.getBalance().toString()
        );

        return "Transfer successful";
    }

    public Page<Transaction> getTransactionHistory(String accountNumber, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository
                .findByAccountNumberOrderByTimestampDesc(accountNumber, pageable);
    }

}
