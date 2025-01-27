package com.volunteerplatform.web.dto;

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
    @NotBlank
    @Size(min = 2, max = 200)
    @UniqueUsername
    private String username;

    @NotEmpty
    @Size(min = 5, max = 200)
    private String fullName;

    @Email
    @UniqueEmail
    private String email;

    @Min(1)
    @Max(90)
    private Integer age;

    @Size(min = 5, max = 100)
    private String password;

    private String confirmPassword;

    private Level level;
}