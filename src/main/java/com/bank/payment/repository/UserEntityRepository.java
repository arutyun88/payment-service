package com.bank.payment.repository;

import com.bank.payment.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByLogin(String login);
}
