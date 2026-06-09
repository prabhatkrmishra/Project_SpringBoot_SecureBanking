package com.security.bank.repository;

import com.security.bank.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Investment} entity.
 */
@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
}