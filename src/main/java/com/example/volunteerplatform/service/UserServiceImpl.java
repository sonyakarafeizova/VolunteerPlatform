package com.example.volunteerplatform.service;

import com.example.volunteerplatform.data.RoleRepository;
import com.example.volunteerplatform.data.UserRepository;
import com.example.volunteerplatform.model.Role;
import com.example.volunteerplatform.model.User;
import com.example.volunteerplatform.model.UserRoles;
import com.example.volunteerplatform.service.dtos.UserProfileDTO;
import com.example.volunteerplatform.web.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(UserRoles.USER)
                .orElseThrow(() -> new RuntimeException("Default role not found."));
        user.getRoles().add(userRole);
        return userRepository.save(user);
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {

    }

    @Override
    public UserProfileDTO getProfileData() {
        return new UserProfileDTO("John Doe", "john.doe@example.com", List.of("USER"));
    }

    @Override
    public boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public boolean isUsernameUnique(String username) {
        return !userRepository.existsByEmail(username);
    }
}
