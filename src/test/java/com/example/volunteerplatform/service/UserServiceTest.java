package com.example.volunteerplatform.service;

import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.UserHelperService;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.web.dto.UserRegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService toTest;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private UserHelperService mockUserHelperService;

    @BeforeEach
    void setUp() {
        toTest = new UserService(
                mockUserRepository,
                mockPasswordEncoder,
                new ModelMapper(),
                mockUserHelperService
        );
    }

    @Test
    void testUserRegistration() {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("TestUser");
        userRegisterDTO.setFullName("Test User");
        userRegisterDTO.setAge(23);
        userRegisterDTO.setPassword("topsecret");
        userRegisterDTO.setEmail("anna@example.com");

        when(mockPasswordEncoder.encode(userRegisterDTO.getPassword()))
                .thenReturn("encodedPassword");

        // Act
        toTest.register(userRegisterDTO);

        // Assert
        verify(mockUserRepository).save(userCaptor.capture());
        User actualSavedEntity = userCaptor.getValue();

        assertNotNull(actualSavedEntity);
        assertEquals(userRegisterDTO.getUsername(), actualSavedEntity.getUsername());
        assertEquals(userRegisterDTO.getFullName(), actualSavedEntity.getFullName());
        assertEquals("encodedPassword", actualSavedEntity.getPassword());
        assertEquals(userRegisterDTO.getEmail(), actualSavedEntity.getEmail());
    }
}
