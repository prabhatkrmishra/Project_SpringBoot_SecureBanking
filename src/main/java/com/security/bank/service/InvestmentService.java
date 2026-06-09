package com.security.bank.service;

import com.security.bank.dto.InvestmentDto;

/**
 * Service for investment operations.
 */
public interface InvestmentService {
    String investNow(Long accountId, InvestmentDto investmentDto);
}