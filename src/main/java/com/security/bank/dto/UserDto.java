package com.security.bank.dto;

import com.security.bank.entity.Account;
import com.security.bank.entity.Investment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for user registration. Contains personal details and
 * initialized lists for accounts and investments (not used during creation).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private String username;
    private String password;
    private String address;
    private Long number;
    private String identityProof;
    private List<Account> accountList = new ArrayList<>();
    private List<Investment> investmentList = new ArrayList<>();
}