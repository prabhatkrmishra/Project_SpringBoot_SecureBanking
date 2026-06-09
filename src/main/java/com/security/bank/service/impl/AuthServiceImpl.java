package com.security.bank.service.impl;

import com.security.bank.dto.JwtRequest;
import com.security.bank.dto.JwtResponse;
import com.security.bank.security.CustomUserDetailService;
import com.security.bank.security.JwtAuthenticationHelper;
import com.security.bank.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Handles user authentication and JWT generation.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailService;
    private final JwtAuthenticationHelper jwtHelper;

    public AuthServiceImpl(AuthenticationManager authenticationManager, CustomUserDetailService userDetailService, JwtAuthenticationHelper jwtHelper) {
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtHelper = jwtHelper;
    }

    /**
     * Authenticates the user and returns a JWT token.
     *
     * @param request containing username and password
     * @return {@link JwtResponse} with the token
     * @throws BadCredentialsException if credentials are invalid
     */
    @Override
    public JwtResponse login(JwtRequest request) {
        doAuthenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userDetailService.loadUserByUsername(request.getUsername());
        String token = jwtHelper.generateToken(userDetails);
        return new JwtResponse(token);
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(auth);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}