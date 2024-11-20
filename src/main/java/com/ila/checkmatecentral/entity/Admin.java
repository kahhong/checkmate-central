package com.ila.checkmatecentral.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Admin extends AccountCredential {
    @OneToMany(mappedBy = "admin", targetEntity = Tournament.class)
    @JsonManagedReference
    private Set<Tournament> tournaments;


    public Admin(String email, String name, String password) {
        super(email, name, password, "ROLE_ADMIN");
    }
}
