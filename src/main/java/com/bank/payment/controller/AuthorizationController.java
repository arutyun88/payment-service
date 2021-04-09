package com.bank.payment.controller;

import com.bank.payment.configuration.jwt.JwtProvider;
import com.bank.payment.model.dto.RegistrationRequestDto;
import com.bank.payment.model.dto.RequestDto;
import com.bank.payment.model.dto.ResponseUserDto;
import com.bank.payment.model.UserEntity;
import com.bank.payment.service.UserService;
import org.springframework.web.bind.annotation.*;

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
    public ResponseUserDto auth(@RequestBody RequestDto request) {
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity.getLogin());
        return new ResponseUserDto(token);
    }

//    todo перед каждым запросом проверять в jwtFilter на соответствие предоставленного пользователем токена хранящемуся в бд user_token
//    todo user может осуществлять платежи только если авторизован со своим токеном

    //todo список всех платежей всех пользователей - доступ только для админа
    @GetMapping("/admin/{admin_name}/payments")
    public ResponseUserDto getPaymentsForAdmin(@PathVariable(name = "admin_name") String adminName) {
        UserEntity userEntity = userService.findByLogin(adminName);
        String login = userEntity.getLogin();
        return new ResponseUserDto(login);
    }

    //todo список всех платежей пользователя - доступ для авторизованного пользователя
    @GetMapping("/user/{user}/payments")
    public ResponseUserDto getPaymentsForUser(@PathVariable(name = "user") String username) {
        UserEntity userEntity = userService.findByLogin(username);
        String login = userEntity.getLogin();
        return new ResponseUserDto(login);
    }

    //todo выход пользователя из системы - token размещается в игнор лист в бд
    @PostMapping("/user/{user}/logout")
    public String logout(@PathVariable(name = "user") String username) {
        UserEntity userEntity = userService.findByLogin(username);
        String login = userEntity.getLogin();
        return "OK" + " " + login;
    }
}
