package com.example.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.data.PictureRepository;
import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.UserHelperService;
import com.volunteerplatform.service.dtos.CauseDetailsDTO;
import com.volunteerplatform.service.dtos.CauseShortInfoDTO;
import com.volunteerplatform.web.dto.AddCauseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CauseServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private CauseRepository causeRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserHelperService userHelperService;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CauseService causeService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGetAllCauses() {

        RestClient.RequestHeadersUriSpec requestSpec = mock(RestClient.RequestHeadersUriSpec.class, RETURNS_SELF);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        List<CauseShortInfoDTO> dummyList = Collections.emptyList();

        when(restClient.get()).thenReturn(requestSpec);
        when(requestSpec.uri(eq("/causes"))).thenReturn(requestSpec);
        when(requestSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(dummyList);

        List<CauseShortInfoDTO> result = causeService.getAllCauses();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetCauseById() {
        Long id = 1L;
        CauseDetailsDTO dummyDetails = new CauseDetailsDTO();
        dummyDetails.setId(id);
        dummyDetails.setTitle("Test Cause");

        RestClient.RequestHeadersUriSpec requestSpec = mock(RestClient.RequestHeadersUriSpec.class, RETURNS_SELF);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.get()).thenReturn(requestSpec);
        when(requestSpec.uri(eq("/causes/{id}"), eq(id))).thenReturn(requestSpec);
        when(requestSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(eq(CauseDetailsDTO.class))).thenReturn(dummyDetails);

        CauseDetailsDTO result = causeService.getCauseById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test Cause", result.getTitle());
    }


    @Test
    void testGetCauseById_NullId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> causeService.getCauseById(null));
    }

    @Test
    void testCreateCause() {
        AddCauseDTO dto = new AddCauseDTO();
        dto.setTitle("New Cause");
        dto.setDescription("Test Description");

        RestClient.RequestBodyUriSpec requestBodySpec = mock(RestClient.RequestBodyUriSpec.class, RETURNS_SELF);

        RestClient.RequestHeadersUriSpec postResponseSpec = mock(RestClient.RequestHeadersUriSpec.class, RETURNS_SELF);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.post()).thenReturn(requestBodySpec);
        when(requestBodySpec.uri(eq("/causes"))).thenReturn(requestBodySpec);
        when(requestBodySpec.body(eq(dto))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);

        causeService.createCause(dto);

        verify(restClient).post();
        verify(requestBodySpec).uri("/causes");
        verify(requestBodySpec).body(dto);
    }

    @Test
    void testDeleteCauseFromApi() {
        Long id = 2L;
        RestClient.RequestHeadersUriSpec requestSpec = mock(RestClient.RequestHeadersUriSpec.class, RETURNS_SELF);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.delete()).thenReturn(requestSpec);
        when(requestSpec.uri(eq("/causes/{id}"), eq(id))).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);

        causeService.deleteCauseFromApi(id);
        verify(restClient).delete();
        verify(requestSpec).uri("/causes/{id}", id);
    }


    @Test
    void testGetCauseDetails() {
        Long id = 3L;
        CauseDetailsDTO dummyDetails = new CauseDetailsDTO();
        dummyDetails.setId(id);
        dummyDetails.setTitle("Details Cause");

        RestClient.RequestHeadersUriSpec requestSpec = mock(RestClient.RequestHeadersUriSpec.class, RETURNS_SELF);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.get()).thenReturn(requestSpec);

        when(requestSpec.uri(eq("/causes/{id}"))).thenReturn(requestSpec);
        when(requestSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(eq(CauseDetailsDTO.class))).thenReturn(dummyDetails);

        CauseDetailsDTO result = causeService.getCauseDetails(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }


    @Test
    void testFindByUser_WithNoCauses() {
        User user = new User();
        when(causeRepository.findByAuthor(user)).thenReturn(Collections.emptyList());
        List<Cause> result = causeService.findByUser(user);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void testFindByUser_WithCauses() {
        User user = new User();
        Cause cause1 = new Cause();
        cause1.setId(1L);
        Cause cause2 = new Cause();
        cause2.setId(2L);
        when(causeRepository.findByAuthor(user)).thenReturn(List.of(cause1, cause2));
        List<Cause> result = causeService.findByUser(user);
        assertNotNull(result);
        assertEquals(2, result.size());
    }


    @Test
    void testRemoveCausesOlderThanMonths() {
        int months = 6;

        when(causeRepository.deleteByCreatedBefore(any(LocalDateTime.class))).thenReturn(3);
        int deleted = causeService.removeCausesOlderThanMonths(months);
        assertEquals(3, deleted);
    }
}
