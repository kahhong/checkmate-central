package com.ila.checkmatecentral;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerCreateForm {
    @Size(min=3, max=12)
    @NotEmpty(message="Player ID is necessary!")
    private String playername;

    @NotEmpty(message="Password is necessary!")
    private String password1;

    @NotEmpty(message="Confirmation of password is necessary!")
    private String password2;

    @NotEmpty(message="Email is necessary!")
    @Email
    private String email;
}
