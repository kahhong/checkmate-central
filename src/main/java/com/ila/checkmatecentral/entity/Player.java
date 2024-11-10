package com.ila.checkmatecentral.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Player extends AccountCredential{
    private double rating;

    private double ratingDeviation;

    private LocalDateTime timeLastPlayed;

    @ManyToOne
    @JoinColumn(name="tournamentId")
    @JsonBackReference
    private Tournament tournament;

    
    public Player(String email, String name, String password) {
        super(email, name, password, "ROLE_USER");
        this.rating = 1500.00;
        this.ratingDeviation = 350.00;
        this.timeLastPlayed = LocalDateTime.now();
    }
}
