package com.bank.payment.controller;

import com.bank.payment.configuration.jwt.JwtProvider;
import com.bank.payment.model.UserEntity;
import com.bank.payment.model.dto.ErrorMessage;
import com.bank.payment.model.dto.RegistrationRequestDto;
import com.bank.payment.model.dto.RequestDto;
import com.bank.payment.model.dto.ResponseUserDto;
import com.bank.payment.service.TokenService;
import com.bank.payment.service.UserService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
public class AuthorizationController {
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;

    public AuthorizationController(UserService userService,
                                   TokenService tokenService,
                                   JwtProvider jwtProvider) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequestDto registrationRequestDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(registrationRequestDto.getPassword());
        userEntity.setLogin(registrationRequestDto.getLogin());
        userEntity.setFirstName(registrationRequestDto.getFirstName());
        userEntity.setLastName(registrationRequestDto.getLastName());
        userEntity.setCurrencyType("USD");
        userEntity.setBalance(8.0d);
        userEntity.setActive(true);
        userService.saveUser(userEntity, registrationRequestDto.getRole());
        return ResponseEntity.status(HttpStatus.OK).body("User " + userEntity.getLogin() + " registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestDto request) {
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (userEntity == null) {
            log.severe("User " + request.getLogin() + " is not found");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessage.builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("Authorization error. " + "User " + request.getLogin() +
                            " is not found." + " Please, change your password and login again!")
                    .build());
        }
        String token = jwtProvider.generateToken(userEntity.getLogin());
        return ResponseEntity.ok(new ResponseUserDto(token));
    }

//    todo перед каждым запросом проверять в jwtFilter на соответствие предоставленного пользователем токена хранящемуся в бд token_table
//    todo ? требуется ли это, если onFilter осуществляет проверку каждого запроса
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

    @PostMapping("/user/{user}/logout")
    public ResponseEntity<?> logout(@PathVariable(name = "user") String username) {
        String token = jwtProvider.getToken();
        if (!tokenService.checkToken(token)) {
            log.severe("Access denied. Please register or login again!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessage.builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("Access denied. Please register or login again!")
                    .build());
        }
        UserEntity userEntity = userService.findByLogin(username);
        tokenService.addTokenInIgnoreList(token, userEntity);
        return ResponseEntity.ok(username + " logged out");
    }

    @PostMapping("/user/payment")
    public ResponseEntity<?> payment() {
        String token = jwtProvider.getToken();
        if (!tokenService.checkToken(token)) {
            log.severe("Access denied. Please register or login again!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessage.builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("Access denied. Please register or login again!")
                    .build());
        }
        String username = jwtProvider.getLoginFromToken(token);
        UserEntity userEntity = userService.findByLogin(username);
        if (userEntity.getBalance() > 1.1) {
            userEntity.setBalance(userEntity.getBalance() - 1.1);
            userService.updateBalance(userEntity);
            return ResponseEntity.ok().body("User " + userEntity.getLogin() + " make a payment!");
        } else {
            log.severe("Not enough money to make a payment!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessage.builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("Not enough money to make a payment!")
                    .build());
        }
    }
}
