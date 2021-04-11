package com.bank.payment.repository;

import com.bank.payment.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByLogin(String login);
    List<UserEntity> findAllByLogin(String login);
}
