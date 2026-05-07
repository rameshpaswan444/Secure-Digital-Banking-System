package com.banking.Admin;

import com.banking.Entity.Account;
import com.banking.Entity.Transaction;
import com.banking.Repository.AccountRepository;
import com.banking.Repository.TransactionRepository;
import com.banking.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DashboardResponse getDashboardStats() {

        // 👥 total users
        long totalUsers = userRepository.count();

        // 🏦 total accounts
        long totalAccounts = accountRepository.count();

        // 💳 total transactions
        long totalTransactions = transactionRepository.count();

        // 💰 total bank balance
        BigDecimal totalBalance = accountRepository.findAll()
                .stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ➕ total deposits
        BigDecimal totalDeposits = transactionRepository
                .findByType("DEPOSIT")
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ➖ total withdrawals
        BigDecimal totalWithdrawals = transactionRepository
                .findByType("WITHDRAW")
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalAccounts(totalAccounts)
                .totalTransactions(totalTransactions)
                .totalBankBalance(totalBalance)
                .totalDeposits(totalDeposits)
                .totalWithdrawals(totalWithdrawals)
                .build();
    }
}
