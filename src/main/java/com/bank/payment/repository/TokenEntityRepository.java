package com.bank.payment.repository;

import com.bank.payment.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenEntityRepository extends JpaRepository<TokenEntity, Long> {
    boolean findByTokenName(String tokenName);
}
