package com.example.volunteerplatform.service;

import com.volunteerplatform.data.CommentRepository;
import com.volunteerplatform.data.MentoringRepository;
import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.Comment;
import com.volunteerplatform.model.Mentoring;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.MentoringService;
import com.volunteerplatform.service.UserHelperService;
import com.volunteerplatform.web.dto.CreateCommentDTO;
import com.volunteerplatform.web.dto.MentoringCreateDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MentoringServiceTest {

    @Mock
    private MentoringRepository mentoringRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserHelperService userHelperService;
    @Mock
    private ModelMapper modelMapper;

    private MentoringService mentoringService;

    @BeforeEach
    void setUp(){
        mentoringService = new MentoringService(
                mentoringRepository,
                commentRepository,
                userRepository,
                userHelperService,
                modelMapper
        );
    }

    @Test
    void testCreateMentoring_Success() throws IOException {
        MentoringCreateDTO dto = new MentoringCreateDTO();
        dto.setTitle("Mentor Title");
        dto.setDescription("Description");

        User user = new User();
        user.setUsername("testUser");

        when(userHelperService.getUser()).thenReturn(user);

        Mentoring mappedMentoring = new Mentoring();
        mappedMentoring.setId(1L);
        mappedMentoring.setTitle("Mentor Title");
        mappedMentoring.setDescription("Description");
        mappedMentoring.setAuthor(user);

        when(modelMapper.map(dto, Mentoring.class)).thenReturn(mappedMentoring);
        when(mentoringRepository.save(mappedMentoring)).thenReturn(mappedMentoring);

        boolean result = mentoringService.add(dto);
        assertFalse(result);
        verify(mentoringRepository).save(mappedMentoring);
    }

    @Test
    void testGetById_NotFound_ShouldThrow() {
        when(mentoringRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> mentoringService.getById(999L));
    }

    @Test
    void testAddComment_Success() {
        CreateCommentDTO dto = new CreateCommentDTO();
        dto.setMentoringId(5L);
        dto.setContent("Hello");

        User user = new User();
        user.setUsername("tester");
        Mentoring mentoring = new Mentoring();
        mentoring.setId(5L);

        when(mentoringRepository.findById(5L)).thenReturn(Optional.of(mentoring));
        when(userHelperService.getUser()).thenReturn(user);

        Comment savedComment = new Comment();
        savedComment.setId(100L);
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        Comment result = mentoringService.addComment(dto);
        assertEquals(100L, result.getId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testAddComment_MentoringNotFound_ShouldThrow() {
        CreateCommentDTO dto = new CreateCommentDTO();
        dto.setMentoringId(999L);

        when(mentoringRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> mentoringService.addComment(dto));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testAddToFavourites_NotFound_ShouldThrow() {
        when(mentoringRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> mentoringService.addToFavourites(10L, new User()));
        verify(mentoringRepository, never()).save(any(Mentoring.class));
    }

    @Test
    void testRemoveFromFavourites_Success() {
        User user = new User();
        user.setId(1L);

        Mentoring mentoring = new Mentoring();
        mentoring.setId(2L);

        when(mentoringRepository.findById(2L)).thenReturn(Optional.of(mentoring));
        when(userRepository.findByIdWithFavouriteMentorings(1L)).thenReturn(Optional.of(user));

        mentoringService.removeFromFavourites(2L, user);

        assertFalse(mentoring.getFavouriteUsers().contains(user));
        verify(mentoringRepository).save(mentoring);
    }

    @Test
    void testGetMentoringById_NotFound() {
        when(mentoringRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> mentoringService.getMentoringById(999L));
    }

    @Test
    void testDeleteMentoring_Success() {
        Mentoring mentoring = new Mentoring();
        mentoring.setId(5L);

        when(mentoringRepository.findById(5L)).thenReturn(Optional.of(mentoring));
        mentoringService.deleteMentoring(5L);

        verify(mentoringRepository).delete(mentoring);
    }

    @Test
    void testDeleteMentoring_NotFound_ShouldThrow() {
        when(mentoringRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> mentoringService.deleteMentoring(99L));
    }

    @Test
    void testDeleteComment() {

        mentoringService.deleteComment(100L);
        verify(commentRepository).deleteById(100L);
    }
}
