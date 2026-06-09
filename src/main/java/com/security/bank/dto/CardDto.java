package com.security.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for applying for a new card.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private String cardHolderName;
    private String cardType;      // DEBIT_CLASSIC, CREDIT_PREMIUM, etc.
    private Long pin;             // desired PIN
}