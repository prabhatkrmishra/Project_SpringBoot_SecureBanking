package com.security.bank.service.impl;

import com.security.bank.dto.AdminDto;
import com.security.bank.entity.*;
import com.security.bank.repository.AccountRepository;
import com.security.bank.repository.RoleRepository;
import com.security.bank.repository.UserRepository;
import com.security.bank.service.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link AdminService} providing admin user creation,
 * user listing, deletion, and account activation/deactivation.
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new admin user and assigns the ROLE_ADMIN role.
     *
     * @param dto admin details
     * @return the saved admin {@link User}
     */
    @Override
    public User addAdmin(AdminDto dto) {
        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setRoleName("ROLE_ADMIN");
            return roleRepository.saveAndFlush(role);
        });

        adminRole = roleRepository.findByRoleName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        User user = new User();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(dto.getAddress());
        user.setNumber(dto.getNumber());
        user.setIdentityProof(dto.getIdentityProof());
        user.setRoles(adminRole);

        return userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public String deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return "Deleted Successfully";
        }
        return "Error in deletion";
    }

    @Override
    public String deactivateAccount(Long userId, Long accountId) {
        User user = userRepository.findById(userId).orElse(null);
        Account account = accountRepository.findById(accountId).orElse(null);
        if (user == null || account == null) return "ERROR";
        account.setStatus("INACTIVE");
        accountRepository.save(account);
        return "Deactivated Account for User with id: " + userId;
    }

    @Override
    public String activateAccount(Long userId, Long accountId) {
        User user = userRepository.findById(userId).orElse(null);
        Account account = accountRepository.findById(accountId).orElse(null);
        if (user == null || account == null) return "ERROR";
        account.setStatus("ACTIVE");
        accountRepository.save(account);
        return "Activated Account for User with id: " + userId;
    }

    @Override
    public List<Account> getActiveAccounts() {
        return accountRepository.findAllActiveAccounts();
    }

    @Override
    public List<Account> getInactiveAccounts() {
        return accountRepository.findAllInActiveAccounts();
    }

    @Override
    public List<Account> getAccountsByType(AccountType accountType) {
        return accountRepository.findAllByAccountType(accountType);
    }

    @Override
    public List<Account> getAccountsByBranch(BranchType branchType) {
        return accountRepository.findAllByBranch(branchType);
    }
}