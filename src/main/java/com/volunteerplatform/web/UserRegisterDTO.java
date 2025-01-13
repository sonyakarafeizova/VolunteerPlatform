package com.volunteerplatform.web;

import com.volunteerplatform.validation.annotation.UniqueEmail;
import com.volunteerplatform.validation.annotation.UniqueUsername;
import com.volunteerplatform.validation.annotation.ValidatePasswords;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.logging.Level;

@Getter
@Setter
@NoArgsConstructor
@ValidatePasswords
public class UserRegisterDTO {
    @NotEmpty(message = "Username cannot be empty")
    @UniqueUsername
    private String username;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email")
    @UniqueEmail
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Confirm password cannot be empty")
    private String confirmPassword;

    private Level level;
}