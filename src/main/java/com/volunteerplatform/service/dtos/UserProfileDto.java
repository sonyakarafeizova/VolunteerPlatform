package com.volunteerplatform.service.dtos;

import com.volunteerplatform.model.User;
import com.volunteerplatform.model.enums.Level;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserProfileDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private int age;
    private Level level;
    public UserProfileDto(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.age = user.getAge();
        this.level = user.getLevel();
    }

    public UserProfileDto setLevel(Level level) {
        this.level = level;
        return this;
    }
}
