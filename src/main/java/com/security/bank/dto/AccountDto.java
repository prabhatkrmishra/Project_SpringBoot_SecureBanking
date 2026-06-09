package com.security.bank.dto;

import com.security.bank.entity.Nominee;
import lombok.Data;

/**
 * DTO for creating a new bank account.
 */
@Data
public class AccountDto {
    private String accountType;   // SAVINGS, CURRENT, PPF, SALARY
    private double balance;
    private String proof;         // identity proof document reference
    private Nominee nominee;      // optional nominee details
}