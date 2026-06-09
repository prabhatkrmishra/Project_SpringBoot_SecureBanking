package com.security.bank.controller;

import com.security.bank.dto.JwtRequest;
import com.security.bank.dto.JwtResponse;
import com.security.bank.dto.UserDto;
import com.security.bank.entity.User;
import com.security.bank.service.AuthService;
import com.security.bank.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public controller for user registration and login.
 * No authentication required (permitted in security config).
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * Registers a new customer user.
     * Password and sensitive fields are cleared from the response.
     *
     * @param userDto registration data
     * @return the saved user with HTTP 201
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto) {
        User user = userService.register(userDto);
        // Clear sensitive data before returning
        user.setPassword(null);
        user.setInvestmentList(null);
        user.setAccountList(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request login credentials (username, password)
     * @return {@link JwtResponse} containing the JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}