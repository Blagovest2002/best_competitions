package com.example.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ErrorDto {
    private HttpStatus statusCode;
    private LocalDateTime dateTime;
    private String msg;

}
