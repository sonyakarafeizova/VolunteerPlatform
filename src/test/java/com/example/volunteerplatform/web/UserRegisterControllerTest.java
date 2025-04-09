package com.example.volunteerplatform.web;

import com.volunteerplatform.VolunteerPlatformApplication;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.web.dto.UserRegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = VolunteerPlatformApplication.class)
@AutoConfigureMockMvc
public class UserRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    public void testViewRegister() throws Exception {

        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registerData"))
                .andExpect(model().attributeExists("levels"))
                .andExpect(view().name("register"));
    }


    @Test
    public void testDoRegister_Success() throws Exception {

        when(userService.isEmailUnique("new@example.com")).thenReturn(true);
        when(userService.isUsernameUnique("newUser")).thenReturn(true);

        mockMvc.perform(post("/users/register")
                        .param("username", "newUser")
                        .param("fullName", "New User")
                        .param("age", "25")
                        .param("password", "secret")
                        .param("confirmPassword", "secret")
                        .param("email", "new@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        verify(userService).register(any(UserRegisterDTO.class));
    }


    @Test
    public void testDoRegister_BindingErrors() throws Exception {

        mockMvc.perform(post("/users/register")
                        .param("fullName", "New User")
                        .param("age", "25")
                        .param("password", "secret")
                        .param("confirmPassword", "secret")
                        .param("email", "new@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));
        verify(userService, never()).register(any(UserRegisterDTO.class));
    }

    @Test
    public void testDoRegister_UsernameNotUnique() throws Exception {

        when(userService.isUsernameUnique("takenUser")).thenReturn(false);

        mockMvc.perform(post("/users/register")
                        .param("username", "takenUser")
                        .param("fullName", "Full Name")
                        .param("age", "25")
                        .param("password", "secret")
                        .param("confirmPassword", "secret")
                        .param("email", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));

        verify(userService, never()).register(any(UserRegisterDTO.class));
    }
}
