package com.ila.checkmatecentral.entity;

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

import java.util.Collection;
import java.util.List;

@MappedSuperclass
@NoArgsConstructor
public class AccountCredential implements UserDetails, CredentialsContainer {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Getter
    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false)
    private String name;

    @Email
    @Getter
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true, nullable = false)
    private String email;

    @Getter
    @Setter
    @NotBlank(message = "Authority is mandatory")
    private String grantedAuthorityString;

    @NotBlank(message = "Password is mandatory")
    @Column(nullable = false)
    @Setter
    private String password;


    public AccountCredential(String email, String name, String password, String grantedAuthorityString) {
        this.email = email;
        this.password = password;
        this.name = name;
        if(isValidGrantedAuthorityString(grantedAuthorityString)) {
            this.grantedAuthorityString = grantedAuthorityString;
        } else {
            throw new RuntimeException("Invalid Granted Authority String: " + grantedAuthorityString);
        }
    }

    public static boolean isValidGrantedAuthorityString(String grantedAuthorityString) {
        return ("ROLE_USER".equals(grantedAuthorityString) || "ROLE_ADMIN".equals(grantedAuthorityString));
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(grantedAuthorityString));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
