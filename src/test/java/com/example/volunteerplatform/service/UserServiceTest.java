package com.example.volunteerplatform.service;

import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.UserHelperService;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.service.dtos.UserProfileDto;
import com.volunteerplatform.web.dto.UserLoginDTO;
import com.volunteerplatform.web.dto.UserRegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserHelperService userHelperService;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRegisterDTO userRegisterDTO;
    private UserProfileDto userProfileDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFullName("Test User"); // Ensure full name is set

        userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setEmail("test@example.com");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setFullName("Test User"); // Ensure full name is set

        userProfileDto = new UserProfileDto();
    }

    @Test
    void testRegisterUser() {
        when(modelMapper.map(userRegisterDTO, User.class)).thenAnswer(invocation -> {
            User mappedUser = new User();
            mappedUser.setUsername(userRegisterDTO.getUsername());
            mappedUser.setEmail(userRegisterDTO.getEmail());
            mappedUser.setPassword(userRegisterDTO.getPassword());
            mappedUser.setFullName(userRegisterDTO.getFullName()); // Ensure full name is mapped
            return mappedUser;
        });

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertNotNull(savedUser.getFullName()); // Ensure full name is not null
            return savedUser;
        });

        userService.register(userRegisterDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetProfileData() {
        when(userHelperService.getUser()).thenReturn(user);
        when(modelMapper.map(user, UserProfileDto.class)).thenReturn(userProfileDto);

        UserProfileDto result = userService.getProfileData();

        assertNotNull(result);
        verify(userHelperService, times(1)).getUser();
    }

    @Test
    void testIsUsernameUnique() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertTrue(userService.isUsernameUnique("testuser"));
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testAuthenticateUser() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertTrue(userService.authenticateUser(loginDTO));
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }
}
