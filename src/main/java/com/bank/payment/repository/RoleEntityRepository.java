package com.bank.payment.repository;

import com.bank.payment.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}
