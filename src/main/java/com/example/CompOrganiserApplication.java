package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ConfigSec.class)
public class CompOrganiserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompOrganiserApplication.class, args);
    }

}
