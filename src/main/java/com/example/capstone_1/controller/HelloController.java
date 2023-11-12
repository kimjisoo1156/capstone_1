package com.example.capstone_1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping(value = "/hello", produces="application/json; charset=utf8")
    public String hello(){
        return "안녕";
    }

    @GetMapping("/hello2")
    public String hello2(){
        return "hihi";
    }

    @GetMapping("/hello4")
    public String hello4(){
        return "jisoo";
    }
    @GetMapping("/hello3")
    public String hello3(){
        return "jisoo ghkdkdld";
    }
    @GetMapping("/hello5")
    public String hello5(){
        return "서버가혹시?";
    }
}
