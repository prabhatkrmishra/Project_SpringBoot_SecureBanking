package com.security.bank.controller;

import com.security.bank.dto.InvestmentDto;
import com.security.bank.service.InvestmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for customer investment operations.
 * Requires CUSTOMER role.
 */
@RestController
@RequestMapping("/invest")
public class UserInvestmentController {

    private final InvestmentService investmentService;

    public UserInvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    /**
     * Makes a new investment from an account.
     *
     * @param accountId     the source account ID
     * @param investmentDto investment details (type, amount, duration)
     * @return HTTP 200 with success message
     */
    @PostMapping("/now")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> investNow(@RequestParam Long accountId, @RequestBody InvestmentDto investmentDto) {
        String result = investmentService.investNow(accountId, investmentDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}