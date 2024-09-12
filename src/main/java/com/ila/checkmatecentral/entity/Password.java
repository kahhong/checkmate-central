package com.ila.checkmatecentral.entity;

import java.security.SecureRandom;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Password {
    
    private static final int SALT_LENGTH = 16;
    
    @Id
    private final Long userId;
    
    @NotNull
    @Column(length = SALT_LENGTH)
    private final byte[] salt;

    @Id
    @NotBlank(message = "Password is mandatory")
    private final String hashedPassword;
    
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(updatable = false)
    @NotNull
    private Date dateCreated;

    public Password(long userId, byte[] salt, String password) {
        Assert.isTrue(salt != null && salt.length == SALT_LENGTH, "Salt must be %d bytes".formatted(SALT_LENGTH));

        this.userId = userId;
        this.salt = salt;

        hashedPassword = encoder.encode(salt + password);
    }

    public Password(long userId, String password) {
        this.userId = userId;

        salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        hashedPassword = encoder.encode(salt + password);
    }
    
    @Override
    public String toString() {
        return hashedPassword;
    }
    
    private static PasswordEncoder encoder;
    
    private static SecureRandom random;

    @Autowired
    public static void setPasswordEncoder(PasswordEncoder encoder) {
        Password.encoder = encoder;
    }
    
    public static void setSecureRandom(SecureRandom random) {
        Password.random = random;
    }
}
