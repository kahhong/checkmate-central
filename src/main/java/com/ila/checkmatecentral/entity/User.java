package com.ila.checkmatecentral.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
public class User implements UserDetails, CredentialsContainer {
    
    public static final GrantedAuthority ROLE_PLAYER = new SimpleGrantedAuthority("PLAYER");
    public static final GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ADMIN");
    
    @Id
    @GeneratedValue
    @Getter
    private Long id;
    
    @Email
    @Getter
    @NotBlank(message = "Email is mandatory")
    private final String email;
    
    @Getter
    @NotBlank(message = "Name is mandatory")
    private final String name;
    
    @Transient
    private String password;
    
    @NotNull
    @OneToOne(cascade = CascadeType.REMOVE)
    @MapsId
    private Password hashedPassword;

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.hashedPassword = new Password(id, password);
    }
    
    public User(String email, String name, Password password) {
        this.email = email;
        this.name = name;
        this.hashedPassword = password;
    }

    @Override
    public void eraseCredentials() {
        hashedPassword = null;
        password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(ROLE_PLAYER);
    }

    @Override
    public String getPassword() {
        return hashedPassword.toString();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
