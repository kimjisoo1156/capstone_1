package com.example.capstone_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Capstone1Application {
    public static void main(String[] args) {
        SpringApplication.run(Capstone1Application.class, args);
    }
}
