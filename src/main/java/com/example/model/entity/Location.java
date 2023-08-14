package com.example.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "city_id",referencedColumnName = "id")
    private City city;
    @Column(name = "address")
    private String address;
    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<Event> events;
}
