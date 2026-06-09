package com.security.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating user KYC (Know Your Customer) details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KycDto {
    private String name;
    private String address;
    private Long number;
    private String identityProof;
}