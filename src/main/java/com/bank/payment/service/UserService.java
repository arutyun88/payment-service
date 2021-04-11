package com.bank.payment.service;

import com.bank.payment.model.RoleEntity;
import com.bank.payment.model.UserEntity;
import com.bank.payment.repository.RoleEntityRepository;
import com.bank.payment.repository.UserEntityRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserEntityRepository userEntityRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserEntityRepository userEntityRepository,
            RoleEntityRepository roleEntityRepository,
            PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.roleEntityRepository = roleEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(UserEntity userEntity, String role) {
        userEntity.setRoleEntity("admin".equals(role) ?
                roleEntityRepository.findByName("ROLE_ADMIN") :
                roleEntityRepository.findByName("ROLE_USER"));
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        List<UserEntity> userEntities = userEntityRepository.findAllByLogin(userEntity.getLogin());
        if (userEntities != null) {
            for (UserEntity entity : userEntities) {
                if (userEntity.getFirstName().equals(entity.getFirstName()) &&
                        userEntity.getLastName().equals(entity.getLastName())) {
                    log.info("User " + userEntity.getLogin() + " registered early");
                    return;
                }
            }
        }
        userEntityRepository.save(userEntity);
        log.info("User " + userEntity.getLogin() + " registered");
    }

    public UserEntity findByLogin(String login) {
        return userEntityRepository.findByLogin(login);
    }

    public UserEntity findByLoginAndPassword(String login, String password) {
        UserEntity userEntity = findByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }

    public void updateBalance(UserEntity userEntity) {
        userEntityRepository.save(userEntity);
        log.info("User " + userEntity.getLogin() + " make a payment!");
    }
}
