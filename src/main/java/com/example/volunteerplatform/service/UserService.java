package com.example.volunteerplatform.service;

import com.example.volunteerplatform.model.User;
import com.example.volunteerplatform.web.UserRegisterDTO;

import java.util.Optional;

public interface UserService {

    User registerUser(User user);
    Optional<User> findByUsername(String username);

    void register(UserRegisterDTO userRegisterDTO);

    Object getProfileData();

    boolean isEmailUnique(String email);

    boolean isUsernameUnique(String username);
}
