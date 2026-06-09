package com.security.bank.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class providing thread‑safe random number generation
 * for account and card numbers, as well as CVV.
 */
public final class AccountUtility {

    private AccountUtility() {
        // prevent instantiation
    }

    /**
     * Generates a 10‑digit account number.
     */
    public static Long generateAccountNumber() {
        return ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L);
    }

    /**
     * Generates a 16‑digit card number.
     */
    public static Long generateCardNumber() {
        return ThreadLocalRandom.current().nextLong(1000000000000000L, 9999999999999999L);
    }

    /**
     * Generates a 3‑digit CVV (100–999).
     */
    public static int generateCvv() {
        return ThreadLocalRandom.current().nextInt(100, 999);
    }
}