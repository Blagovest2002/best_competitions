package com.example.model.entity;

import jakarta.persistence.*;
import jakarta.validation.OverridesAttribute;
import lombok.Data;

import java.util.List;
@Data
@Entity(name  = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Event event;
    @OneToMany(mappedBy = "category")
    private List<Participant> participants;
    @OneToOne
    @JoinColumn(name = "weight_class_id",referencedColumnName = "id")
    private WeightClass weightClass;
}
