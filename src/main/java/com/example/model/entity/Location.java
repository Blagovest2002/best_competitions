package com.example.model.entity;
import jakarta.validation.OverridesAttribute;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
@Data
@Entity(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "city_id")
    private City city;
    @OneToMany(mappedBy = "location")
    private List<Event> events;

}
