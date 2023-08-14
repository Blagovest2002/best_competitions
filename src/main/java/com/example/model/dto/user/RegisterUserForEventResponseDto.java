package com.example.model.dto.user;

import lombok.Data;

@Data
public class RegisterUserForEventResponseDto {
    private String firstName;
    private String lastName;
    private int weightClass;
}
