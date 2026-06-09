package com.security.bank.controller;

import com.security.bank.dto.AdminDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.AccountType;
import com.security.bank.entity.BranchType;
import com.security.bank.entity.User;
import com.security.bank.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for administrative operations.
 * All endpoints are prefixed with "/admin" and require ROLE_ADMIN.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Creates a new admin user.
     *
     * @param adminDto admin details
     * @return the saved admin {@link User}
     */
    @PostMapping("/add")
    public User addAdmin(@RequestBody AdminDto adminDto) {
        return adminService.addAdmin(adminDto);
    }

    /**
     * Returns all registered users.
     *
     * @return list of {@link User}
     */
    @GetMapping("/getAllUser")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the unique username
     * @return the matching {@link User}
     */
    @GetMapping("/getUserByName/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return adminService.getUserByUsername(username);
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId the ID of the user to delete
     * @return deletion status message
     */
    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        return adminService.deleteUser(userId);
    }

    /**
     * Deactivates a user's account (sets status to INACTIVE).
     *
     * @param userId    the owner's ID
     * @param accountId the account ID to deactivate
     * @return response entity with status message
     */
    @PutMapping("/account/deactivate")
    public ResponseEntity<String> deactivateAccount(@RequestParam Long userId, @RequestParam Long accountId) {
        String message = adminService.deactivateAccount(userId, accountId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
    }

    /**
     * Activates a previously deactivated account.
     *
     * @param userId    the owner's ID
     * @param accountId the account ID to activate
     * @return response entity with status message
     */
    @PutMapping("/account/activate")
    public ResponseEntity<String> activateAccount(@RequestParam Long userId, @RequestParam Long accountId) {
        String message = adminService.activateAccount(userId, accountId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
    }

    /**
     * Lists all accounts with status ACTIVE.
     *
     * @return list of active accounts
     */
    @GetMapping("/account/getActiveAccountsList")
    public List<Account> getActiveAccounts() {
        return adminService.getActiveAccounts();
    }

    /**
     * Lists all accounts with status INACTIVE.
     *
     * @return list of inactive accounts
     */
    @GetMapping("/account/getInActiveAccountsList")
    public List<Account> getInactiveAccounts() {
        return adminService.getInactiveAccounts();
    }

    /**
     * Finds accounts by their type (e.g., SAVINGS, CURRENT).
     *
     * @param accType the {@link AccountType} enum value
     * @return list of matching accounts
     */
    @GetMapping("/accountList/ByAccountType/{accType}")
    public List<Account> getAccountsByType(@PathVariable AccountType accType) {
        return adminService.getAccountsByType(accType);
    }

    /**
     * Finds accounts by branch type (e.g., SBI, HDFC).
     *
     * @param branchType the {@link BranchType} enum value
     * @return list of matching accounts
     */
    @GetMapping("/accountList/ByBranchType/{branchType}")
    public List<Account> getAccountsByBranch(@PathVariable BranchType branchType) {
        return adminService.getAccountsByBranch(branchType);
    }
}