package com.security.bank.service.impl;

import com.security.bank.dto.CardDto;
import com.security.bank.entity.*;
import com.security.bank.repository.AccountRepository;
import com.security.bank.repository.CardRepository;
import com.security.bank.service.CardService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Implementation of {@link CardService} for blocking, applying, and updating cards.
 */
@Service
public class CardServiceImpl implements CardService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    public CardServiceImpl(AccountRepository accountRepository, CardRepository cardRepository) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public String blockCard(Long accountNumber, Long cardNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("No Account Found"));
        Card card = cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> new RuntimeException("No Card Found"));

        if (account.getCard() != null && account.getCard().getCardNumber().equals(card.getCardNumber())) {
            account.setCard(null);
            accountRepository.save(account);
            cardRepository.deleteById(card.getId());
            return "Card Blocked Successfully";
        }
        throw new RuntimeException("Card not linked to this account");
    }

    @Override
    public String applyNewCard(Long accountNumber, CardDto cardDto) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getCard() != null && account.getCard().getCardNumber() != null) {
            throw new RuntimeException("Account already has a card.");
        }

        Card card = new Card();
        card.setCardNumber(generateCardNumber());
        card.setCvv(generateCvv());
        card.setCardHolderName(cardDto.getCardHolderName() != null && !cardDto.getCardHolderName().trim().isEmpty() ? cardDto.getCardHolderName() : account.getUser().getName());
        card.setPin(cardDto.getPin() != null && cardDto.getPin() != 0 ? cardDto.getPin() : 1234L);

        CardType cardType = resolveCardType(cardDto.getCardType(), account.getAccountType());
        card.setCardType(cardType);
        setDailyLimit(card, cardType);

        card.setAllocationDate(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 5);
        card.setExpiryDate(cal.getTime());
        card.setStatus("ACTIVE");

        Card savedCard = cardRepository.save(card);
        account.setCard(savedCard);
        accountRepository.save(account);
        return "New Card Allocated to account with Number: " + accountNumber;
    }

    @Override
    public Card updateCardSettings(Card card, Long cardNumber) {
        Card existingCard = cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> new RuntimeException("Card not found"));
        if (card.getDailyLimit() > 0) {
            validateLimit(existingCard.getCardType(), card.getDailyLimit());
            existingCard.setDailyLimit(card.getDailyLimit());
        }
        if (card.getPin() != null && card.getPin() != 0) {
            existingCard.setPin(card.getPin());
        }
        return cardRepository.save(existingCard);
    }

    private CardType resolveCardType(String cardTypeStr, AccountType accountType) {
        if (cardTypeStr != null && !cardTypeStr.trim().isEmpty()) {
            try {
                return CardType.valueOf(cardTypeStr.toUpperCase().trim());
            } catch (IllegalArgumentException ignored) {
            }
        }
        return getDefaultCardTypeForAccount(accountType);
    }

    private CardType getDefaultCardTypeForAccount(AccountType accountType) {
        switch (accountType) {
            case SAVINGS:
                return CardType.DEBIT_CLASSIC;
            case CURRENT:
                return CardType.DEBIT_GLOBAL;
            case SALARY:
                return CardType.CREDIT_MASTER;
            case PPF:
                return CardType.CREDIT_PREMIUM;
            default:
                return CardType.DEBIT_CLASSIC;
        }
    }

    private void setDailyLimit(Card card, CardType cardType) {
        switch (cardType) {
            case DEBIT_CLASSIC:
                card.setDailyLimit(20000);
                break;
            case DEBIT_GLOBAL:
                card.setDailyLimit(40000);
                break;
            case CREDIT_PREMIUM:
                card.setDailyLimit(50000);
                break;
            case CREDIT_MASTER:
                card.setDailyLimit(75000);
                break;
            default:
                card.setDailyLimit(20000);
        }
    }

    private void validateLimit(CardType cardType, double limit) {
        switch (cardType) {
            case DEBIT_CLASSIC:
                if (limit > 40000) throw new RuntimeException("Daily limit cannot exceed 40000");
                break;
            case DEBIT_GLOBAL:
                if (limit > 50000) throw new RuntimeException("Daily limit cannot exceed 50000");
                break;
            case CREDIT_PREMIUM:
                if (limit > 75000) throw new RuntimeException("Daily limit cannot exceed 75000");
                break;
            case CREDIT_MASTER:
                if (limit > 100000) throw new RuntimeException("Daily limit cannot exceed 100000");
                break;
        }
    }

    private Long generateCardNumber() {
        Random random = new Random();
        long min = 100000000000000L;
        long max = 999999999999999L;
        return min + (long) (random.nextDouble() * (max - min));
    }

    private int generateCvv() {
        return new Random().nextInt(900) + 100;
    }
}