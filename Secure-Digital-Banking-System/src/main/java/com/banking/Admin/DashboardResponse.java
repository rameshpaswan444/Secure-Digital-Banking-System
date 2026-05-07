package com.banking.Admin;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalUsers;

    private Long totalAccounts;

    private Long totalTransactions;

    private BigDecimal totalBankBalance;

    private BigDecimal totalDeposits;

    private BigDecimal totalWithdrawals;
}