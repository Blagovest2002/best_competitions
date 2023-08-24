package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Validator;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;

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
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register the JSR310 module
        return objectMapper;
    }
    /*@Bean
    public HandlerExceptionResolver handlerExceptionResolver(){
        return new HandlerExceptionResolverComposite();
    }*/

    public static void main(String[] args) {
        SpringApplication.run(CompOrganiserApplication.class, args);
    }

}
