package com.example.volunteerplatform.service.dtos;

import com.example.volunteerplatform.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileDTO {
    private String username;
    private String fullName;
    private int age;
    private Role role;

    public <E> UserProfileDTO(String johnDoe, String mail, List<E> user) {
    }
}
