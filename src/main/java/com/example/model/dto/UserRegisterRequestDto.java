package com.example.model.dto;

import com.example.model.entity.Role;
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
    private String email;
    private String password;
    @JsonAlias("phone_number")
    private String phoneNumber;
    private int age;
}
