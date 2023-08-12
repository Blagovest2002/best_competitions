package com.example.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}
