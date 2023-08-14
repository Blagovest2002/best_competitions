package com.example.model.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class RegisterUserForEventDto {
    @JsonAlias("weight_class_id")
    private int weightClassId;
}
