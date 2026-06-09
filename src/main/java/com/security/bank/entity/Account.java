package com.security.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a bank account linked to a user.
 */
@Entity
@Table(name = "accounts")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;   // SAVINGS, CURRENT, PPF, SALARY

    private String status;             // ACTIVE or INACTIVE

    private double balance;

    private float interestRate;

    @Enumerated(EnumType.STRING)
    private BranchType branch;         // SBI, ICIC, BOB, HDFC

    private String proof;              // identity proof document

    private Date openingDate;

    private Long accountNumber;        // unique 8–10 digit number

    @OneToOne(cascade = CascadeType.ALL)
    private Nominee nominee;

    @OneToOne(cascade = CascadeType.ALL)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;                 // owning user, ignored during serialization
}