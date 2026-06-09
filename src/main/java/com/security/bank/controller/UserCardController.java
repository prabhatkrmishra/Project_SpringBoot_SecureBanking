package com.security.bank.controller;

import com.security.bank.dto.CardDto;
import com.security.bank.entity.Card;
import com.security.bank.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for customer card operations (block, apply, update settings).
 * Requires CUSTOMER role.
 */
@RestController
@RequestMapping("/card")
public class UserCardController {

    private final CardService cardService;

    public UserCardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Blocks a card permanently and removes its link from the account.
     *
     * @param accountNumber the account number
     * @param cardNumber    the card number to block
     * @return success message
     */
    @GetMapping("/block")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String blockCard(@RequestParam Long accountNumber, @RequestParam Long cardNumber) {
        return cardService.blockCard(accountNumber, cardNumber);
    }

    /**
     * Applies for a new card for the given account.
     *
     * @param accountNumber the account number
     * @param cardDto       card application details
     * @return HTTP 201 with success message
     */
    @PostMapping("/apply/new")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> applyNewCard(@RequestParam Long accountNumber, @RequestBody CardDto cardDto) {
        String message = cardService.applyNewCard(accountNumber, cardDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    /**
     * Updates card settings (daily limit, PIN).
     *
     * @param card       card entity containing new values
     * @param cardNumber the card number to update
     * @return the updated {@link Card}
     */
    @PutMapping("/setting")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Card updateSettings(@RequestBody Card card, @RequestParam Long cardNumber) {
        return cardService.updateCardSettings(card, cardNumber);
    }
}