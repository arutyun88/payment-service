package com.bank.payment.service;

import com.bank.payment.model.TokenEntity;
import com.bank.payment.model.UserEntity;
import com.bank.payment.repository.TokenEntityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class TokenService {
    private final TokenEntityRepository tokenEntityRepository;

    @Value("${jwt.token.expired}")
    private int expiredDate;

    public TokenService(TokenEntityRepository tokenEntityRepository) {
        this.tokenEntityRepository = tokenEntityRepository;
    }

    public void addTokenInIgnoreList(String token, UserEntity user) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setTokenName(token);
        tokenEntity.setActive(false);
        tokenEntity.setExpireDate(addDate(new Date()));
        tokenEntity.setUser_id(user);
        tokenEntityRepository.save(tokenEntity);
    }

    private Date addDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, expiredDate);
        return calendar.getTime();
    }
}
