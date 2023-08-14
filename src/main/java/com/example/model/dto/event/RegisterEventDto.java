package com.example.model.dto.event;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterEventDto {
    @JsonAlias("event_name")
    private String eventName;
    @JsonAlias("city_id")
    private int cityId;
    private String address;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
}
