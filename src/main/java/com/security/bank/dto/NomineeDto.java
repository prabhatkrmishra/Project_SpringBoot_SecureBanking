package com.security.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating nominee information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NomineeDto {
    private String relation;
    private String name;
    private Long accountNumber;   // nominee's own account number
    private String gender;
    private int age;
}