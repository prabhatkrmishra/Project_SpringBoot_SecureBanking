package com.security.bank.controller;

import com.security.bank.dto.AccountDto;
import com.security.bank.dto.KycDto;
import com.security.bank.dto.NomineeDto;
import com.security.bank.dto.UserKycDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.Nominee;
import com.security.bank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for customer account operations.
 * All methods require the CUSTOMER role.
 */
@RestController
@RequestMapping("/account")
public class UserAccountController {

    private final AccountService accountService;

    public UserAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Creates a new bank account for a given user.
     *
     * @param dto    account details
     * @param userId the owning user's ID
     * @return the created {@link Account} with HTTP 201
     */
    @PostMapping("/create/{userId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto dto, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(dto, userId));
    }

    /**
     * Returns all accounts belonging to a user.
     *
     * @param userId the user's ID
     * @return list of accounts
     */
    @GetMapping("/all/{userId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Account> getAllAccounts(@PathVariable Long userId) {
        return accountService.getAllAccounts(userId);
    }

    /**
     * Retrieves the balance of an account by its number.
     *
     * @param accountNumber the account number
     * @return current balance
     */
    @GetMapping("/balance")
    @PreAuthorize("hasRole('CUSTOMER')")
    public double getBalance(@RequestParam Long accountNumber) {
        return accountService.getBalance(accountNumber);
    }

    /**
     * Fetches the nominee associated with an account.
     *
     * @param accountNumber the account number
     * @return the {@link Nominee} details
     */
    @GetMapping("/nominee")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Nominee getNominee(@RequestParam Long accountNumber) {
        return accountService.getNominee(accountNumber);
    }

    /**
     * Updates the nominee for a given account.
     *
     * @param nomineeDto new nominee data
     * @param accountId  the account ID to update
     * @return the updated nominee
     */
    @PutMapping("/updateNominee/{accountId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Nominee updateNominee(@RequestBody NomineeDto nomineeDto, @PathVariable Long accountId) {
        return accountService.updateNominee(nomineeDto, accountId);
    }

    /**
     * Returns KYC details for the user linked to the account.
     * In case of error, an empty DTO is returned.
     *
     * @param accountNumber the account number
     * @return {@link UserKycDto} with user KYC information
     */
    @GetMapping("/getKycDetails")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<UserKycDto> getKycDetails(@RequestParam Long accountNumber) {
        try {
            return ResponseEntity.ok(accountService.getKycDetails(accountNumber));
        } catch (Exception ex) {
            return ResponseEntity.ok(new UserKycDto());
        }
    }

    /**
     * Updates KYC information for the user owning the given account.
     *
     * @param dto       new KYC data
     * @param accountId the account ID
     * @return the updated KYC information
     */
    @PutMapping("/updateKyc/{accountId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<UserKycDto> updateKyc(@RequestBody KycDto dto, @PathVariable Long accountId) {
        try {
            return ResponseEntity.ok(accountService.updateKyc(dto, accountId));
        } catch (Exception ex) {
            return ResponseEntity.ok(new UserKycDto());
        }
    }

    /**
     * Provides a summary of the account, excluding user details.
     *
     * @param accountNumber the account number
     * @return a simplified {@link Account} object
     */
    @GetMapping("/getAccount/summary")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Account getAccountSummary(@RequestParam Long accountNumber) {
        return accountService.getAccountSummary(accountNumber);
    }
}