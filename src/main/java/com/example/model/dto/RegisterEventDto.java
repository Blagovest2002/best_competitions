package com.example.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class RegisterEventDto {
    @JsonAlias("event_name")
    private String eventName;
    @JsonAlias("city_id")
    private int cityId;
}
