package com.bank.payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin/test")
    public String getAdmin(){
        return "WELCOME TO ADMIN PAGE";
    }

    @GetMapping("/user/test")
    public String getUser() {
        return "WELCOME TO USER PAGE";
    }
}
