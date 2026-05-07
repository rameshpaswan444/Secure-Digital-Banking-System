package com.banking.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendAccountCreationEmail(String toEmail, String accountNumber) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Bank Account Created Successfully 🏦");

        message.setText(
                "Dear Customer,\n\n" +
                        "Your bank account has been created successfully.\n\n" +
                        "Account Number: " + accountNumber + "\n\n" +
                        "Thank you for choosing our bank.\n\n" +
                        "Regards,\nBanking System Team"
        );

        mailSender.send(message);
    }

    public void sendDepositEmail(
            String toEmail,
            String accountNumber,
            String amount,
            String balance
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject("Deposit Successful 💰");

        message.setText(
                "Dear Customer,\n\n" +
                        "A deposit has been made to your account.\n\n" +
                        "Account Number: " + accountNumber + "\n" +
                        "Amount Deposited: " + amount + "\n" +
                        "Current Balance: " + balance + "\n\n" +
                        "Thank you for banking with us."
        );

        mailSender.send(message);
    }

    public void sendWithdrawEmail(
            String toEmail,
            String accountNumber,
            String amount,
            String balance
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject("Withdrawal Successful 💸");

        message.setText(
                "Dear Customer,\n\n" +
                        "A withdrawal has been made from your account.\n\n" +
                        "Account Number: " + accountNumber + "\n" +
                        "Amount Withdrawn: " + amount + "\n" +
                        "Remaining Balance: " + balance + "\n\n" +
                        "Thank you for banking with us."
        );

        mailSender.send(message);
    }

    public void sendTransferEmail(
            String toEmail,
            String fromAccount,
            String toAccount,
            String amount,
            String balance
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject("Money Transfer Successful 🔁");

        message.setText(
                "Dear Customer,\n\n" +
                        "Money transfer completed successfully.\n\n" +
                        "From Account: " + fromAccount + "\n" +
                        "To Account: " + toAccount + "\n" +
                        "Transferred Amount: " + amount + "\n" +
                        "Remaining Balance: " + balance + "\n\n" +
                        "Thank you for banking with us."
        );

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(
            String toEmail,
            String token
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject("Password Reset Request 🔐");

        message.setText(
                "Dear Customer,\n\n" +
                        "Use this token to reset your password:\n\n" +
                        token +
                        "\n\nThis token expires in 15 minutes."
        );

        mailSender.send(message);
    }
}
