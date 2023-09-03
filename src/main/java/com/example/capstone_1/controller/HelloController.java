package com.example.capstone_1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "안녕";
    }

    @GetMapping("/hello2")
    public String hello2(){
        return "hi";
    }

}
