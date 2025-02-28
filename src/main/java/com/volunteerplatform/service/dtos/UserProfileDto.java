package com.volunteerplatform.service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.logging.Level;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileDto {
    private String username;
    private String fullName;
    private int age;
    private Level level;

    public UserProfileDto(String username, String fullName, Integer age, com.volunteerplatform.model.Level level) {
    }
}
