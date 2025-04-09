package com.example.volunteerplatform.web;

import com.volunteerplatform.VolunteerPlatformApplication;
import com.volunteerplatform.model.Role;
import com.volunteerplatform.model.User;
import com.volunteerplatform.model.enums.UserRoles;
import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.service.dtos.UserProfileDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = VolunteerPlatformApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CauseService causeService;

    @Test
    @WithMockUser(username="adminUser",roles={"ADMIN"})
    public void testDashboard_WithAdminRole() throws Exception {
        User adminUser = new User();
        adminUser.setUsername("adminUser");
        Role adminRole = new Role();
        adminRole.setRole(UserRoles.ADMIN);
        adminUser.setRoles(Set.of(adminRole));

        when(userService.findByUsername("adminUser")).thenReturn(adminUser);

        mockMvc.perform(get("/users/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));
    }

    @Test
    @WithMockUser(username="normalUser",roles={"USER"})
    public void testDashboard_WithNormalUser() throws Exception {
        User normal = new User();
        normal.setUsername("normalUser");
        when(userService.findByUsername("normalUser")).thenReturn(normal);

        mockMvc.perform(get("/users/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));
    }

    @Test
    @WithMockUser(username="testUser",roles={"USER"})
    public void testProfile() throws Exception {

        User dbUser = new User();
        dbUser.setUsername("testUser");
        dbUser.setId(1L);
        when(userService.findByUsername("testUser")).thenReturn(dbUser);

        when(causeService.findByUser(dbUser)).thenReturn(Collections.emptyList());
        when(userService.getProfileData()).thenReturn(new UserProfileDto());

        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("causes"))
                .andExpect(model().attributeExists("profileData"))
                .andExpect(view().name("profile"));
    }

    @Test
    @WithMockUser(username="ghostUser")
    public void testProfile_UserNotFound_ShouldThrow() throws Exception {
        when(userService.findByUsername("ghostUser")).thenReturn(null);
        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    Exception ex = result.getResolvedException();
                    assertNotNull(ex);
                    assertTrue(ex instanceof RuntimeException);
                    assertEquals("User not found", ex.getMessage());
                });
    }


    @Test
    @WithMockUser(username="editor",roles={"USER"})
    public void testViewProfile() throws Exception {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(10L);
        userProfileDto.setUsername("editor");

        User user = new User();
        user.setId(10L);
        user.setUsername("editor");

        when(userService.getUserProfileById(10L)).thenReturn(userProfileDto);
        when(userService.findUserById(10L)).thenReturn(user);
        when(causeService.findByUser(user)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users/10/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profileData"))
                .andExpect(model().attributeExists("causes"))
                .andExpect(view().name("profile"));
    }
}
