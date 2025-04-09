package com.example.volunteerplatform.service;

import com.volunteerplatform.data.CommentRepository;
import com.volunteerplatform.model.Comment;
import com.volunteerplatform.model.Mentoring;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.CommentService;
import com.volunteerplatform.service.MentoringService;
import com.volunteerplatform.service.UserHelperService;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.service.dtos.CreateCommentApiDTO;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository mockCommentRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserHelperService mockUserHelperService;
    @Mock
    private UserService mockUserService;
    @Mock
    private MentoringService mockMentoringService;

    @Captor
    private ArgumentCaptor<Comment> commentCaptor;

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(
                mockCommentRepository,
                modelMapper,
                mockUserHelperService,
                mockUserService,
                mockMentoringService
        );
    }

    @Test
    void testCreate_WithValidData() {

        CreateCommentDTO dto = new CreateCommentDTO();
        dto.setContent("Valid comment");
        dto.setMentoringId(10L);

        User user = new User();
        user.setUsername("testUser");

        Mentoring mentoring = new Mentoring();
        mentoring.setId(10L);

        when(mockUserHelperService.getUser()).thenReturn(user);
        when(mockMentoringService.getMentoringById(10L)).thenReturn(mentoring);
        when(mockCommentRepository.save(any(Comment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        commentService.create(dto);


        verify(mockCommentRepository).save(commentCaptor.capture());
        Comment saved = commentCaptor.getValue();
        assertEquals("Valid comment", saved.getTextContent());
        assertEquals(user, saved.getAuthor());
        assertEquals(mentoring, saved.getMentoring());
        assertNotNull(saved.getCreated());
    }

    @Test
    void testCreateApi_ShouldReturnApiDto() {

        CreateCommentDTO dto = new CreateCommentDTO();
        dto.setContent("Api comment");
        dto.setMentoringId(1L);

        User user = new User();
        user.setUsername("apiUser");

        Mentoring mentoring = new Mentoring();
        mentoring.setId(1L);

        Comment savedComment = new Comment();
        savedComment.setId(100L);
        savedComment.setTextContent("Api comment");
        savedComment.setAuthor(user);
        savedComment.setMentoring(mentoring);
        savedComment.setCreated(Instant.now());

        when(mockUserHelperService.getUser()).thenReturn(user);
        when(mockMentoringService.getMentoringById(1L)).thenReturn(mentoring);
        when(mockCommentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CreateCommentApiDTO mappedDto = new CreateCommentApiDTO();
        mappedDto.setId(100L);

        when(modelMapper.map(eq(savedComment), eq(CreateCommentApiDTO.class))).thenReturn(mappedDto);


        CreateCommentApiDTO result = commentService.createApi(dto);


        assertEquals(100L, result.getId());
        verify(mockCommentRepository).save(any(Comment.class));
        verify(modelMapper).map(savedComment, CreateCommentApiDTO.class);
    }

    @Test
    void testDelete() {

        Long commentId = 999L;
        commentService.delete(commentId);
        verify(mockCommentRepository).deleteById(commentId);
    }

    @Test
    void testCreate_WhenMentoringNotFound_ShouldThrow() {

        CreateCommentDTO dto = new CreateCommentDTO();
        dto.setMentoringId(999L);

        when(mockMentoringService.getMentoringById(999L))
                .thenThrow(new RuntimeException("Mentoring not found"));

        assertThrows(RuntimeException.class, () -> commentService.create(dto));
        verify(mockCommentRepository, never()).save(any(Comment.class));
    }


    @Test
    void testCreate_WhenUserIsNull_ShouldHandleGracefully() {
        CreateCommentDTO dto = new CreateCommentDTO();
        dto.setContent("Comment with null user");
        dto.setMentoringId(1L);

        when(mockUserHelperService.getUser()).thenReturn(null);
        assertThrows(NullPointerException.class, () -> commentService.create(dto));
        verify(mockCommentRepository, never()).save(any());
    }
}
