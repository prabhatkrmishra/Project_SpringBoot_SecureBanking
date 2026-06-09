package com.security.bank.service.impl;

import com.security.bank.dto.AccountDto;
import com.security.bank.dto.KycDto;
import com.security.bank.dto.NomineeDto;
import com.security.bank.dto.UserKycDto;
import com.security.bank.entity.*;
import com.security.bank.repository.*;
import com.security.bank.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Implementation of {@link AccountService} handling account creation,
 * balance retrieval, nominee/KYC management, and account summary.
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final NomineeRepository nomineeRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public AccountServiceImpl(AccountRepository accountRepository, NomineeRepository nomineeRepository, UserRepository userRepository, CardRepository cardRepository) {
        this.accountRepository = accountRepository;
        this.nomineeRepository = nomineeRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    /**
     * Creates a new account along with an auto‑generated card (except for PPF).
     * Assigns branch, interest rate, and card type based on account type.
     *
     * @param accountDto account details
     * @param userId     owner user ID
     * @return the saved {@link Account}
     */
    @Override
    public Account createAccount(AccountDto accountDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        Card card = new Card();

        // Set common card fields
        card.setCardNumber(generateCardNumber());
        card.setCvv(generateCvv());
        card.setCardHolderName(user.getName());
        card.setStatus("ACTIVE");
        card.setPin(1122L);
        card.setAllocationDate(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 5);
        card.setExpiryDate(calendar.getTime());

        AccountType accountType = AccountType.valueOf(accountDto.getAccountType().toUpperCase());

        // Configure account and card based on type
        switch (accountType) {
            case SAVINGS:
                card.setCardType(CardType.DEBIT_GLOBAL);
                card.setDailyLimit(40000);
                cardRepository.save(card);
                account.setAccountType(AccountType.SAVINGS);
                account.setInterestRate(2.70F);
                account.setBranch(BranchType.BOB);
                account.setCard(card);
                break;

            case CURRENT:
                card.setCardType(CardType.CREDIT_PREMIUM);
                card.setDailyLimit(50000);
                cardRepository.save(card);
                account.setAccountType(AccountType.CURRENT);
                account.setInterestRate(5.2F);
                account.setBranch(BranchType.ICIC);
                account.setCard(card);
                break;

            case PPF:
                // PPF accounts do not receive a card
                account.setAccountType(AccountType.PPF);
                account.setInterestRate(7.4F);
                account.setBranch(BranchType.SBI);
                break;

            case SALARY:
                card.setCardType(CardType.CREDIT_MASTER);
                card.setDailyLimit(75000);
                cardRepository.save(card);
                account.setAccountType(AccountType.SALARY);
                account.setInterestRate(4.1F);
                account.setBranch(BranchType.HDFC);
                account.setCard(card);
                break;

            default:
                throw new RuntimeException("No AccountType Selected");
        }

        account.setAccountNumber(generateRandomNumber());
        account.setStatus("ACTIVE");
        account.setBalance(accountDto.getBalance());
        account.setProof(accountDto.getProof());
        account.setOpeningDate(new Date());
        account.setUser(user);

        // Save nominee if provided
        if (accountDto.getNominee() != null) {
            Nominee nominee = nomineeRepository.save(accountDto.getNominee());
            account.setNominee(nominee);
        }

        return accountRepository.save(account);
    }

    /**
     * Retrieves all accounts belonging to a user.
     */
    @Override
    public List<Account> getAllAccounts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getAccountList();
    }

    /**
     * Returns the balance of the account with the given number.
     */
    @Override
    public double getBalance(Long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getBalance();
    }

    /**
     * Retrieves the nominee linked to an account.
     */
    @Override
    public Nominee getNominee(Long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getNominee();
    }

    /**
     * Updates nominee details for an account.
     */
    @Override
    public Nominee updateNominee(NomineeDto nomineeDto, Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        Nominee nominee = account.getNominee();
        nominee.setName(nomineeDto.getName());
        nominee.setAccountNumber(nomineeDto.getAccountNumber());
        nominee.setRelation(nomineeDto.getRelation());
        nominee.setAge(nomineeDto.getAge());
        nominee.setGender(nomineeDto.getGender());
        accountRepository.save(account);
        return nominee;
    }

    /**
     * Builds and returns KYC details of the account owner.
     */
    @Override
    public UserKycDto getKycDetails(Long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));
        User user = account.getUser();
        UserKycDto dto = new UserKycDto();
        dto.setName(user.getName());
        dto.setAddress(user.getAddress());
        dto.setNumber(user.getNumber());
        dto.setIdentityProof(user.getIdentityProof());
        return dto;
    }

    /**
     * Updates KYC fields for the user owning the account. Only non‑empty/non‑null
     * fields from the DTO are applied.
     */
    @Override
    public UserKycDto updateKyc(KycDto kycDto, Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        User user = account.getUser();

        if (!kycDto.getName().isEmpty()) user.setName(kycDto.getName());
        if (!kycDto.getAddress().isEmpty()) user.setAddress(kycDto.getAddress());
        if (kycDto.getNumber() != null) user.setNumber(kycDto.getNumber());
        if (!kycDto.getIdentityProof().isEmpty()) user.setIdentityProof(kycDto.getIdentityProof());

        userRepository.save(user);

        UserKycDto dto = new UserKycDto();
        dto.setName(user.getName());
        dto.setAddress(user.getAddress());
        dto.setNumber(user.getNumber());
        dto.setIdentityProof(user.getIdentityProof());
        return dto;
    }

    /**
     * Returns account summary without the user association (to avoid circular refs).
     */
    @Override
    public Account getAccountSummary(Long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setUser(null);
        return account;
    }

    private Long generateRandomNumber() {
        Random random = new Random();
        return 10000000L + random.nextLong(90000000L); // 8-digit
    }

    private Long generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return Long.valueOf(sb.toString());
    }

    private int generateCvv() {
        Random random = new Random();
        return random.nextInt(900) + 100; // 100-999
    }
}