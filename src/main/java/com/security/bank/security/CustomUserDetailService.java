package com.security.bank.security;

import com.security.bank.entity.User;
import com.security.bank.exception.ResourceNotFoundException;
import com.security.bank.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Loads user-specific data for authentication.
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user by username and returns a Spring Security UserDetails object.
     *
     * @param username the username identifying the user
     * @return a fully populated user object implementing {@link UserDetails}
     * @throws ResourceNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found : " + username));
        return user;
    }
}