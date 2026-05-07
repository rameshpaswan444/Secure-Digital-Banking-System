package com.banking.Controller;

import com.banking.Dtos.AccountRequest;
import com.banking.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public String createAccount(
            @RequestBody AccountRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();

        return accountService.createAccount(email, request);
    }
}