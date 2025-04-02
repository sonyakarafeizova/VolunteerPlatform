package com.volunteerplatform.service;

import com.volunteerplatform.data.RoleRepository;
import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.exception.ObjectNotFoundException;
import com.volunteerplatform.model.Role;
import com.volunteerplatform.model.User;
import com.volunteerplatform.model.enums.Level;
import com.volunteerplatform.model.enums.UserRoles;
import com.volunteerplatform.service.dtos.UserProfileDto;
import com.volunteerplatform.web.dto.UserLoginDTO;
import com.volunteerplatform.web.dto.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;
    private final RoleRepository roleRepository;

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


    public boolean isUsernameUnique(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }


    public boolean authenticateUser(UserLoginDTO loginData) {
        Optional<User> userOptional = userRepository.findByEmail(loginData.getEmail());
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();


        return passwordEncoder.matches(loginData.getPassword(), user.getPassword());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, User.class))
                .orElseThrow(() -> new RuntimeException("User not found"));

    }


    @Transactional
    public void updateUserRole(Long id, UserRoles role) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        Role roleEntity = roleRepository.findByRole(role).orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roleSet = new HashSet<>();

        roleSet.add(roleEntity);
        user.setRoles(roleSet);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public void updateUserProfile(UserProfileDto userProfileDto) {
        User user = this.userRepository.findById(userProfileDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException(
                        "User with id " + userProfileDto.getId() + " was not found!"));

        user.setFullName(userProfileDto.getFullName())
                .setLevel(Level.valueOf(userProfileDto.getLevel().toString()))
                .setEmail(userProfileDto.getEmail())
                .setAge(userProfileDto.getAge());


        this.userRepository.save(user);

    }

    public UserProfileDto getUserEditDetails(Long id) {
        return this.userRepository.findById(id)
                .map(userEntity -> modelMapper.map(userEntity, UserProfileDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("User with ID " + id + " not found!"));
    }

    public boolean usernameExists(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public UserProfileDto getUserProfileById(Long id) {

        return userRepository.findById(id)
                .map(user -> new UserProfileDto(user))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}


