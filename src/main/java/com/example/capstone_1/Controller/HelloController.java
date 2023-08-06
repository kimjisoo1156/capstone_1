package com.example.capstone_1.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hi2";
    }

    @GetMapping("/")
    public String jisoo(){
        return "제발요";
    }

}
