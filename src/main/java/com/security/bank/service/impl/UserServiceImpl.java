package com.security.bank.service.impl;

import com.security.bank.dto.UserDto;
import com.security.bank.entity.Role;
import com.security.bank.entity.User;
import com.security.bank.repository.RoleRepository;
import com.security.bank.repository.UserRepository;
import com.security.bank.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserService} for customer registration.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new customer and assigns the ROLE_CUSTOMER role.
     *
     * @param dto registration details
     * @return the saved user entity
     */
    @Override
    public User register(UserDto dto) {
        Role role = roleRepository.findByRoleName("ROLE_CUSTOMER").orElseGet(() -> {
            Role customer = new Role();
            customer.setRoleName("ROLE_CUSTOMER");
            return roleRepository.saveAndFlush(customer);
        });

        role = roleRepository.findByRoleName("ROLE_CUSTOMER").orElseThrow(() -> new RuntimeException("ROLE_CUSTOMER not found"));

        User user = new User();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(dto.getAddress());
        user.setNumber(dto.getNumber());
        user.setIdentityProof(dto.getIdentityProof());
        user.setRoles(role);

        return userRepository.saveAndFlush(user);
    }
}