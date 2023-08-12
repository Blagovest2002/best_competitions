package com.example.model.entity;

import jakarta.persistence.*;
import jdk.jfr.Registered;
import lombok.*;

import java.util.List;



@Data
@Entity(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String token;
    @Column(name = "is_revoked")
    private String isRevoked;
    @Column(name = "is_expired")
    private String isExpired;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
