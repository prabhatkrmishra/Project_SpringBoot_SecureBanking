package com.security.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for login response containing the JWT token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String jwtToken;
}