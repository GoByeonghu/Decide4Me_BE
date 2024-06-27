package com.sfz.rest_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/hello")
    public String sayHello() {
        return "안녕하세요, 스티븐임다";
    }
}