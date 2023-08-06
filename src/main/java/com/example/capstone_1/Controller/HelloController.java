package com.example.capstone_1.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hi";
    }

    @GetMapping("/")
    public String jisoo(){
        return "jisoolove";
    }

}