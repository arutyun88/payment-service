package com.bank.payment.service;

import com.bank.payment.model.RoleEntity;
import com.bank.payment.model.UserEntity;
import com.bank.payment.repository.RoleEntityRepository;
import com.bank.payment.repository.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
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
        userEntityRepository.save(userEntity);
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
}
