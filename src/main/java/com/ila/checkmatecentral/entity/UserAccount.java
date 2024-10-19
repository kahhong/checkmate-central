package com.ila.checkmatecentral.entity;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
public class UserAccount implements UserDetails, CredentialsContainer {
    
    @Id
    @GeneratedValue
    @Getter
    private Long id;
    
    @Email
    @Getter
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true, nullable = false)
    private String email;
    
    @Getter
    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false)
    private String name;

    @Getter
    @NotBlank(message = "Authority is mandatory")
    private String grantedAuthorityString;
    
    @NotBlank(message = "Password is mandatory")
    @Column(nullable = false)
    private String password;
    
    @Getter
    @Setter
    private double rating;

    @Getter
    @Setter
    private double ratingDeviation;

    @Getter
    @Setter
    private LocalDateTime timeLastPlayed;


    @ManyToOne
    @JoinColumn(name="tournamentId")
    @Getter
    @Setter
    @JsonBackReference
    private Tournament tournament;

    protected UserAccount() {
    }
    
    public UserAccount(String email, String name, String password, String grantedAuthority) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.rating = 1500.00;
        this.ratingDeviation = 350.00;
        this.timeLastPlayed = LocalDateTime.now();
        this.grantedAuthorityString = grantedAuthority;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }

    public boolean validAuthority() {
        return ("ROLE_USER".equals(grantedAuthorityString) || "ROLE_ADMIN".equals(grantedAuthorityString));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(grantedAuthorityString));
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @Transient
    public String getUsername() {
        return email;
    }
}
