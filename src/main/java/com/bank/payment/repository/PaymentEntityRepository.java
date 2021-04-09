package com.bank.payment.repository;

import com.bank.payment.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentEntityRepository extends JpaRepository<PaymentEntity, Long> {
}
