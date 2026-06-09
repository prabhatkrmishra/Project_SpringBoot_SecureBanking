package com.security.bank.repository;

import com.security.bank.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Card} entity.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(Long cardNumber);
}