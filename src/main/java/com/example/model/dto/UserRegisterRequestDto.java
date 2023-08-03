package com.example.model.dto;

import com.example.model.entity.Role;
import com.example.utility.ValidEmail;
import com.example.utility.ValidPassword;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.Data;

@Data

public class UserRegisterRequestDto {
    @JsonAlias("role_id")
    private int roleId;
    @JsonAlias("first_name")
    private String firstName;
    @JsonAlias("last_name")
    private String lastName;
    @ValidEmail(message = "The email is not valid")
    private String email;
    @ValidPassword
    private String password;
    @JsonAlias("confirm_password")
    private String confirmPassword;
    @JsonAlias("phone_number")
    private String phoneNumber;
    private int age;
}
