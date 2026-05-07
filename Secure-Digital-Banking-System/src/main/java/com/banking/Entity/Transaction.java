package com.banking.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // DEPOSIT, WITHDRAW, TRANSFER

    private BigDecimal amount;

    private LocalDateTime timestamp;

    private String accountNumber;

    private String description;
}
