package com.banking.Service;

import com.banking.Dtos.AccountRequest;
import com.banking.Dtos.ForgotPasswordRequest;
import com.banking.Dtos.ResetPasswordRequest;
import com.banking.Dtos.TransferRequest;
import com.banking.Entity.Account;
import com.banking.Entity.Transaction;
import com.banking.Entity.User;
import com.banking.Exception.ResourceNotFoundException;
import com.banking.Repository.AccountRepository;
import com.banking.Repository.TransactionRepository;
import com.banking.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TransactionRepository transactionRepository;

    // 🏦 Create Account
    public String createAccount(String email, AccountRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (accountRepository.findByUserId(user.getId()).isPresent()) {
            throw new ResourceNotFoundException("Account already exists");
        }

        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(request.getInitialDeposit() != null ?
                        request.getInitialDeposit() : BigDecimal.ZERO)
                .user(user)
                .build();

        accountRepository.save(account);

        // 📧 SEND EMAIL HERE
        emailService.sendAccountCreationEmail(user.getEmail(), accountNumber);

        return "Account created successfully: " + accountNumber;
    }
    // 🔢 Generate Account Number
    private String generateAccountNumber() {
        return "ACC" + (100000 + new Random().nextInt(900000));
    }




}