package com.example.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
//todo make sure that more than one equal weight_class is not added
@Data
@Entity(name = "weight_classes")
public class WeightClass {
    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "event_id",referencedColumnName = "id")
    @JsonIgnore
    private Event event;
    @Column(name = "weight_class")
    private int weightClass;
}
