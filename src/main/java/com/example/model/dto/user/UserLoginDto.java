package com.example.model.dto.user;

import com.example.utility.ValidEmail;
import com.example.utility.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
 @ValidEmail
 private String email;
 @ValidPassword
 private String password;
}
