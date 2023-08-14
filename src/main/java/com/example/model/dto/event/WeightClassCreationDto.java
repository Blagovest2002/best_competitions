package com.example.model.dto.event;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class WeightClassCreationDto {
    @JsonAlias("weight_class")
    private int weightClass;
}
