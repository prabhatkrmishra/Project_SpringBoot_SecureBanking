package com.security.bank.service;

import com.security.bank.dto.JwtRequest;
import com.security.bank.dto.JwtResponse;

/**
 * Service for handling authentication and JWT generation.
 */
public interface AuthService {
    JwtResponse login(JwtRequest request);
}