package com.security.bank.service.impl;

import com.security.bank.dto.InvestmentDto;
import com.security.bank.entity.*;
import com.security.bank.repository.AccountRepository;
import com.security.bank.repository.InvestmentRepository;
import com.security.bank.service.InvestmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link InvestmentService} that deducts the investment amount
 * from the account balance and creates an investment record.
 */
@Service
public class InvestmentServiceImpl implements InvestmentService {

    private final AccountRepository accountRepository;
    private final InvestmentRepository investmentRepository;

    public InvestmentServiceImpl(AccountRepository accountRepository, InvestmentRepository investmentRepository) {
        this.accountRepository = accountRepository;
        this.investmentRepository = investmentRepository;
    }

    @Override
    @Transactional
    public String investNow(Long accountId, InvestmentDto investmentDto) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < investmentDto.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        User user = account.getUser();
        Investment investment = buildInvestment(investmentDto);
        investment.setUser(user);

        // Deduct amount from account
        account.setBalance(account.getBalance() - investmentDto.getAmount());

        investmentRepository.save(investment);
        accountRepository.save(account);

        return "Investment successful";
    }

    private Investment buildInvestment(InvestmentDto dto) {
        Investment investment = new Investment();
        InvestmentType investmentType = InvestmentType.valueOf(dto.getInvestmentType().toUpperCase());
        investment.setInvestmentType(investmentType);
        investment.setAmount(dto.getAmount());
        investment.setDuration(dto.getDuration());

        switch (investmentType) {
            case GOLD:
                investment.setRisk("LOW");
                investment.setReturns(8.0f);
                investment.setCompanyName("Gold Trust");
                break;
            case STOCKS:
                investment.setRisk("HIGH");
                investment.setReturns(18.0f);
                investment.setCompanyName("NSE Equity");
                break;
            case MUTUAL_FUND:
                investment.setRisk("MEDIUM");
                investment.setReturns(12.0f);
                investment.setCompanyName("SBI Mutual Fund");
                break;
            case FIXED_DEPOSITS:
                investment.setRisk("LOW");
                investment.setReturns(7.0f);
                investment.setCompanyName("Fixed Deposit Scheme");
                break;
        }
        return investment;
    }
}