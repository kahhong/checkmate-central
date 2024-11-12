package com.ila.checkmatecentral.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Player extends AccountCredential{
    private double rating;

    private double ratingDeviation;

    private LocalDateTime timeLastPlayed;

    private boolean availability;

    private String profileDescription;

    @ManyToOne
    @JoinColumn(name="tournamentId")
    @JsonBackReference
    private Tournament tournament;

    
    public Player(String email, String name, String password) {
        super(email, name, password, "ROLE_USER");
        this.rating = 1500.00;
        this.ratingDeviation = 350.00;
        this.timeLastPlayed = LocalDateTime.now();
        this.availability = false;
    }
}
