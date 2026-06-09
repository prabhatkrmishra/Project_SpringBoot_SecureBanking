package com.security.bank.service;

import com.security.bank.dto.CardDto;
import com.security.bank.entity.Card;

/**
 * Service for card blocking, application, and settings update.
 */
public interface CardService {
    String blockCard(Long accountNumber, Long cardNumber);

    String applyNewCard(Long accountNumber, CardDto cardDto);

    Card updateCardSettings(Card card, Long cardNumber);
}