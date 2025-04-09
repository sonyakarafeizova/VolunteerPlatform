package com.example.volunteerplatform.web;

import com.volunteerplatform.VolunteerPlatformApplication;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = VolunteerPlatformApplication.class)
@AutoConfigureMockMvc
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CauseService causeService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    public void testDashboard() throws Exception {
        when(causeService.getAllCauses()).thenReturn(Collections.emptyList());
        when(userService.getProfileData()).thenReturn(new UserProfileDto());

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allCauses"))
                .andExpect(model().attributeExists("profileData"))
                .andExpect(view().name("dashboard"));
    }
}
