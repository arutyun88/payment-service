package com.bank.payment.service;

import com.bank.payment.model.PaymentEntity;
import com.bank.payment.model.UserEntity;
import com.bank.payment.repository.PaymentEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {
    private final PaymentEntityRepository paymentEntityRepository;

    public PaymentService(PaymentEntityRepository paymentEntityRepository) {
        this.paymentEntityRepository = paymentEntityRepository;
    }

    public void addPayment(UserEntity userEntity) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentValue(1.1d);
        paymentEntity.setUserEntity(userEntity);
        paymentEntityRepository.save(paymentEntity);
    }
}
