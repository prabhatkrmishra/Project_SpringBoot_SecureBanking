package com.security.bank.service;

import com.security.bank.dto.AdminDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.AccountType;
import com.security.bank.entity.BranchType;
import com.security.bank.entity.User;

import java.util.List;

/**
 * Service layer for admin operations.
 */
public interface AdminService {
    User addAdmin(AdminDto adminDto);

    List<User> getAllUsers();

    User getUserByUsername(String username);

    String deleteUser(Long userId);

    String deactivateAccount(Long userId, Long accountId);

    String activateAccount(Long userId, Long accountId);

    List<Account> getActiveAccounts();

    List<Account> getInactiveAccounts();

    List<Account> getAccountsByType(AccountType accountType);

    List<Account> getAccountsByBranch(BranchType branchType);
}