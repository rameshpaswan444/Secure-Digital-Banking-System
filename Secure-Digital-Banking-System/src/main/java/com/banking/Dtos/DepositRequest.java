package com.banking.Dtos;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
    private BigDecimal amount;
}
