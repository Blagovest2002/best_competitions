package com.example.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


import java.sql.Date;
import java.util.List;

@Data
@Entity(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "is_completed")
    private boolean isCompleted;
    @Column(name = "is_open_for_registrations")
    private boolean isOpenForRegistrations;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "date")
    private Date date;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToMany(mappedBy = "event")
    private List<WeightClass> weightClasses;
    @OneToMany(mappedBy = "event")
    private List<Participant> participants;
    @OneToMany(mappedBy = "event")
    private List<Category> categories;

}
