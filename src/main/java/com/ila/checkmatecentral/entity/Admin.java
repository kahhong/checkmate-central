package com.ila.checkmatecentral.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Admin extends AccountCredential {
    @OneToMany
    @JoinColumn(name="tournamentId")
    @JsonBackReference
    private List<Tournament> tournaments;


    public Admin(String email, String name, String password) {
        super(email, name, password, "ROLE_ADMIN");
    }
}
