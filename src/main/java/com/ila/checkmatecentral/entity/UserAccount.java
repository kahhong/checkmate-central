package com.ila.checkmatecentral.entity;

import java.beans.Transient;
import java.util.Collection;
import java.util.List;

import com.ila.checkmatecentral.service.MatchService;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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


    @ManyToOne
    @JoinColumn(name="tournamentId")
    @Getter
    @Setter
    private Tournament tournament;



    protected UserAccount() {
    }
    
    public UserAccount(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.rating = Math.random();
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(ROLE_PLAYER);
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
