package com.example.volunteerplatform.web;

import com.volunteerplatform.VolunteerPlatformApplication;
import com.volunteerplatform.service.CommentService;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest(classes = VolunteerPlatformApplication.class)
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    public void testCreate() throws Exception {
        mockMvc.perform(post("/comments/create")
                        .param("mentoringId", "1")
                        .param("content", "Hello"))
                .andExpect(redirectedUrl("/mentoring/1"));

        verify(commentService, times(1)).create(any(CreateCommentDTO.class));
    }

    @Test
    @WithMockUser
    public void testPostComment() throws Exception {
        mockMvc.perform(post("/")
                        .param("mentoringId", "2")
                        .param("content", "Via root post mapping"))
                .andExpect(redirectedUrl("/mentoring/2"));

        verify(commentService, times(1)).create(any(CreateCommentDTO.class));
    }

    @Test
    @WithMockUser
    public void testDeleteComment() throws Exception {
        mockMvc.perform(post("/comments/delete/10/99"))
                .andExpect(redirectedUrl("/mentoring/10"));

        verify(commentService, times(1)).delete(99L);
    }


    @Test
    public void testCreate_NotAuthenticated() throws Exception {
        mockMvc.perform(post("/comments/create")
                        .param("mentoringId", "1")
                        .param("content", "Hello"))

                .andExpect(redirectedUrl("http://localhost/users/login"));

        verify(commentService, never()).create(any());
    }
}
