package com.tiem.token.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.tiem.token.core",
    "com.tiem.token.test"
})
public class TTokenTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TTokenTestApplication.class, args);
    }
} 