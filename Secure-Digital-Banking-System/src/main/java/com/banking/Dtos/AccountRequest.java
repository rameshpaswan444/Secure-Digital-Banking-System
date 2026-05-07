package com.banking.Dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {

    private BigDecimal initialDeposit;
}
