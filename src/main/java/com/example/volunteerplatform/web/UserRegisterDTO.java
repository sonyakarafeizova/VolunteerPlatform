package com.example.volunteerplatform.web;

import com.example.volunteerplatform.validation.annotation.ValidatePasswords;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ValidatePasswords
public class UserRegisterDTO {
    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, message = "Password must be at least 6 characters.")
    private String password;

    @NotBlank(message = "Full name is required.")
    private String fullName;

    private Integer age;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    private String confirmPassword;
}
