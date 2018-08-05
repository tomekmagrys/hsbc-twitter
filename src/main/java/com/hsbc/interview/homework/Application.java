package com.hsbc.interview.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.hsbc.interview.homework")
@EntityScan(basePackages = {"com.hsbc.interview.homework.db"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}