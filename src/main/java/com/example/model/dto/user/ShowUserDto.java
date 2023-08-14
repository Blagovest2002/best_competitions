package com.example.model.dto.user;

import lombok.Data;
import com.example.model.entity.Role;
//todo show events show matches wins
@Data
public class ShowUserDto {
    private String firstName;
    private String lastName;
    private int age;
    private Role role;
}
