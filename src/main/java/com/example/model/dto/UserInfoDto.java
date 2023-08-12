package com.example.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
