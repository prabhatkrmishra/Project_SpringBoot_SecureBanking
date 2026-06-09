package com.security.bank.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a debit/credit card associated with an account.
 */
@Entity
@Table(name = "cards")
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cardNumber;           // 16-digit card number

    private String cardHolderName;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private double dailyLimit;

    private int cvv;

    private Date allocationDate;

    private Date expiryDate;           // typically 5 years from allocation

    private Long pin;

    private String status;             // ACTIVE or INACTIVE
}