package com.example.model.dto.participant;

import com.example.model.entity.WeightClass;
import lombok.Data;

@Data
public class ShowParticipantDto {
 private int id;
 private String firstName;
 private String lastName;
 private WeightClass weightClass;
}
