package com.ila.checkmatecentral.entity;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
public class UserAccount implements UserDetails, CredentialsContainer {
    public static final GrantedAuthority ROLE_PLAYER = new SimpleGrantedAuthority("PLAYER");
    public static final GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ADMIN");
    
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


    @Setter
    @Getter
    private String grantedAuthorityString;


    protected UserAccount() {
    }
    
    public UserAccount(String email, String name, String password, String grantedAuthority) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.rating = 1500;
        this.ratingDeviation = 350;
        this.timeLastPlayed = LocalDateTime.now();
        this.grantedAuthorityString = grantedAuthority;
    }

    @Override
    public void eraseCredentials() {
        password = null;
        grantedAuthorityString = null;
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
