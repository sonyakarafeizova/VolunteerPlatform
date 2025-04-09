package com.example.volunteerplatform.service.session;

import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.Role;
import com.volunteerplatform.model.User;
import com.volunteerplatform.model.enums.UserRoles;
import com.volunteerplatform.service.session.AppUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class AppUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    private AppUserDetailsService serviceToTest;

    @BeforeEach
    void setUp() {
        serviceToTest = new AppUserDetailsService(mockUserRepository);
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass");
        Role role = new Role();
        role.setRole(UserRoles.USER);
        user.setRoles(Set.of(role));
        when(mockUserRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        UserDetails userDetails = serviceToTest.loadUserByUsername("user1");
        assertNotNull(userDetails);
        assertEquals("user1", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ShouldThrow() {
        when(mockUserRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        assertThrows(
                org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("unknownUser")
        );
    }

}
