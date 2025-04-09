package com.example.volunteerplatform.web;

import com.volunteerplatform.model.User;
import com.volunteerplatform.service.MentoringService;
import com.volunteerplatform.service.UserHelperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest(classes = com.volunteerplatform.VolunteerPlatformApplication.class)
@AutoConfigureMockMvc
public class MentoringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MentoringService mentoringService;

    @MockBean
    private com.volunteerplatform.data.MentoringRepository mentoringRepository;

    @MockBean
    private com.volunteerplatform.data.UserRepository userRepository;

    @MockBean
    private UserHelperService userHelperService;

    @Test
    @WithMockUser
    public void testPostComment() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        when(userHelperService.getUser()).thenReturn(user);

        mockMvc.perform(post("/mentoring/1/comment")
                        .param("content", "Test comment"))
                .andExpect(redirectedUrl("/mentoring"));
    }
}
