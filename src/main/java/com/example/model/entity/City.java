package com.example.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "cities")
public class City {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "city_name")
    private String cityName;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    @OneToOne(mappedBy = "city")
    private Location location;

}
