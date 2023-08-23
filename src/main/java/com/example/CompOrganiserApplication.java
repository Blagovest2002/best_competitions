package com.example;

import jakarta.validation.Validator;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@Import({SecurityConfiguration.class,ConfigSec.class})
@Data
public class CompOrganiserApplication {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }



    public static void main(String[] args) {
        SpringApplication.run(CompOrganiserApplication.class, args);
    }

}
