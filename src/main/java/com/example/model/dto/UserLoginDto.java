package com.example.model.dto;

import com.example.utility.ValidEmail;
import com.example.utility.ValidPassword;
import lombok.Data;

@Data
public class UserLoginDto {
 @ValidEmail
 private String email;
 @ValidPassword
 private String password;
}
