package com.example.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
@Data
@Entity(name = "countries")
public class Country {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;
 @Column(name = "country_name")
 private String countryName;
 @OneToMany(mappedBy = "country")
 List<City> cities;
}
