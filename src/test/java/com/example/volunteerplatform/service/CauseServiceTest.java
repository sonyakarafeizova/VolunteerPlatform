package com.example.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.data.PictureRepository;
import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.UserHelperService;
import com.volunteerplatform.service.dtos.CauseDetailsDTO;
import com.volunteerplatform.web.dto.AddCauseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CauseServiceTest {

    @Mock
    private CauseRepository causeRepository;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserHelperService userHelperService;

    @Mock
    private RestClient restClient;

    @InjectMocks
    private CauseService causeService;

    private Cause cause;
    private User user;
    private AddCauseDTO addCauseDTO;
    private CauseDetailsDTO causeDetailsDTO;

    @BeforeEach
    void setUp() {
        cause = new Cause();
        cause.setId(1L);

        user = new User();
        user.setUsername("testuser");

        addCauseDTO = new AddCauseDTO();
        causeDetailsDTO = new CauseDetailsDTO();
    }

    @Test
    void testGetCauseById() {
        when(causeRepository.findById(1L)).thenReturn(Optional.of(cause));
        when(modelMapper.map(any(Cause.class), eq(CauseDetailsDTO.class))).thenReturn(causeDetailsDTO);

        CauseDetailsDTO result = causeService.getDetails(1L);

        assertNotNull(result);
        verify(causeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByUser() {
        when(causeRepository.findByAuthor(user)).thenReturn(List.of(cause));

        List<Cause> result = causeService.findByUser(user);

        assertFalse(result.isEmpty());
        verify(causeRepository, times(1)).findByAuthor(user);
    }

    @Test
    void testRemoveCausesOlderThanMonths() {
        when(causeRepository.deleteByCreatedBefore(any())).thenReturn(5);

        int deletedCount = causeService.removeCausesOlderThanMonths(6);

        assertEquals(5, deletedCount);
        verify(causeRepository, times(1)).deleteByCreatedBefore(any());
    }
}
