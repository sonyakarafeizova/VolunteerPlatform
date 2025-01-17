package com.volunteerplatform.service;

import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.dtos.UserProfileDto;
import com.volunteerplatform.web.dto.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;

    public void register(UserRegisterDTO userRegisterDTO) {
        User user = this.modelMapper.map(userRegisterDTO, User.class);

        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        userRepository.save(user);
    }

    public UserProfileDto getProfileData() {
        return modelMapper.map(userHelperService.getUser(), UserProfileDto.class);
    }

    public boolean isUsernameUnique(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    public boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }
}