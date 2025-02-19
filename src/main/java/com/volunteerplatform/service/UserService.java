package com.volunteerplatform.service;

import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.dtos.UserProfileDto;
import com.volunteerplatform.web.dto.UserLoginDTO;
import com.volunteerplatform.web.dto.UserRegisterDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;


    public void register(UserRegisterDTO userRegisterDTO) {
        User user = this.modelMapper.map(userRegisterDTO, User.class);
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }

        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        userRepository.save(user);
    }

    public UserProfileDto getProfileData() {
        return modelMapper.map(userHelperService.getUser(), UserProfileDto.class);
    }

    public UserProfileDto getProfileDataByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserProfileDto(user.getUsername(), user.getFullName(), user.getAge(), user.getLevel()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean isUsernameUnique(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }




    public boolean authenticateUser(UserLoginDTO loginData) {
        Optional<User> userOptional= userRepository.findByEmail(loginData.getEmail());
        if (userOptional.isEmpty()) {
            return false; // User not found
        }
        User user = userOptional.get();

        // Check if password matches
        return passwordEncoder.matches(loginData.getPassword(), user.getPassword());
    }
}