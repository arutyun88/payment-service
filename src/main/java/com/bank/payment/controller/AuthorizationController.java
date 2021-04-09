package com.bank.payment.controller;

import com.bank.payment.configuration.jwt.JwtProvider;
import com.bank.payment.model.dto.RegistrationRequestDto;
import com.bank.payment.model.dto.RequestDto;
import com.bank.payment.model.dto.ResponseDto;
import com.bank.payment.model.UserEntity;
import com.bank.payment.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthorizationController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody RegistrationRequestDto registrationRequestDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(registrationRequestDto.getPassword());
        userEntity.setLogin(registrationRequestDto.getLogin());
        userService.saveUser(userEntity);
        return "OK";
    }

    @PostMapping("/login")
    public ResponseDto auth(@RequestBody RequestDto request) {
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity.getLogin());
        return new ResponseDto(token);
    }
}
