package com.banking.jwt;

import com.banking.Dtos.ForgotPasswordRequest;
import com.banking.Dtos.LoginRequest;
import com.banking.Dtos.RegisterRequest;
import com.banking.Dtos.ResetPasswordRequest;
import com.banking.Entity.User;
import com.banking.Exception.ResourceNotFoundException;
import com.banking.Repository.UserRepository;
import com.banking.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public String register(RegisterRequest request) {

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);

        return "User registered successfully";
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid password");
        }

        return jwtService.generateToken(user.getEmail());
    }

    public String forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 🔐 Generate token
        String token = UUID.randomUUID().toString();

        user.setResetToken(token);

        user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        // 📧 Send email
        emailService.sendResetPasswordEmail(
                user.getEmail(),
                token
        );

        return "Password reset email sent";
    }

    public String resetPassword(ResetPasswordRequest request) {

        User user = userRepository
                .findByResetToken(request.getToken())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid token"));

        // ⏰ Check expiry
        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        // 🔒 Encode new password
        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        // 🧹 Clear token
        user.setResetToken(null);
        user.setTokenExpiry(null);

        userRepository.save(user);

        return "Password reset successful";
    }
}
