package com.ila.checkmatecentral.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerLoginForm {
    @NotEmpty(message="Email is necessary!")
    @Email
    private String email;

    @NotEmpty(message="Password is necessary!")
    private String password;
}
