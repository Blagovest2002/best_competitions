package com.example.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private String token ;
    private int id;
    private String firstName;
    private String lastName;
}
