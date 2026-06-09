package com.security.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for registering an admin user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private String name;
    private String username;
    private String password;
    private String address;
    private Long number;          // phone number
    private String identityProof;
}