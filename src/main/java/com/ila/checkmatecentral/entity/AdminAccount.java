package com.ila.checkmatecentral.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
public class AdminAccount implements UserDetails, CredentialsContainer {

    @Getter
    private final String GRANTED_AUTHORITY_STRING = "ROLE_ADMIN";

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

    @ManyToOne
    @JoinColumn(name="tournamentId")
    @Getter
    @Setter
    @JsonBackReference
    private Tournament tournament;


    public AdminAccount(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(GRANTED_AUTHORITY_STRING));
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
