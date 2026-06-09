package com.security.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Nominee details for an account.
 */
@Entity
@Table(name = "nominees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nominee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String relation;        // e.g., spouse, son, daughter

    private String name;

    private Long accountNumber;     // nominee's bank account number

    private String gender;

    private int age;
}