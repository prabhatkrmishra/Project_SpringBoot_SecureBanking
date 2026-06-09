package com.security.bank.service;

import com.security.bank.dto.UserDto;
import com.security.bank.entity.User;

/**
 * Service for user registration.
 */
public interface UserService {
    User register(UserDto userDto);
}