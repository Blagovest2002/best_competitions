package com.example.model.dto.user;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private int id;
    private String firstName;
    private String lastName;
}
