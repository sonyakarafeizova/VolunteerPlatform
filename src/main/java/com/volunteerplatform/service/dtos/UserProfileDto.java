package com.volunteerplatform.service.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.logging.Level;

@Getter
@Setter
public class UserProfileDto {
    private String username;
    private String fullName;
    private int age;
    private Level level;
}
