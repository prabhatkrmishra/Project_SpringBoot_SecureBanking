package com.security.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for making an investment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentDto {
    private String investmentType; // GOLD, STOCKS, MUTUAL_FUND, FIXED_DEPOSITS
    private double amount;
    private String duration;       // e.g., "6 months", "1 year"
}