package com.example.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.data.PictureRepository;
import com.volunteerplatform.model.Cause;
import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.UserHelperService;
import com.volunteerplatform.service.dtos.CauseDetailsDTO;
import com.volunteerplatform.service.dtos.CauseShortInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CauseServiceTest {

    private CauseService toTest;

    @Captor
    private ArgumentCaptor<Cause> causeCaptor;

    @Mock
    private CauseRepository mockCauseRepository;

    @Mock
    private PictureRepository mockPictureRepository;

    @Mock
    private UserHelperService mockUserHelperService;

    @Mock
    private RestClient mockRestClient;

    @BeforeEach
    void setUp() {
        toTest = new CauseService(
                mockRestClient,
                mockCauseRepository,
                new ModelMapper(),
                mockUserHelperService,
                mockPictureRepository
        );
    }

    @Test
    void testGetAllCauses_ShouldReturnEmptyList() {
        when(mockRestClient.get()
                .uri("/causes")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CauseShortInfoDTO>>() {
                }))
                .thenReturn(Collections.emptyList());

        List<CauseShortInfoDTO> causes = toTest.getAllCauses();

        assertNotNull(causes);
        assertTrue(causes.isEmpty());
    }

    @Test
    void testGetCauseById_ShouldReturnCorrectCause() {
        Long causeId = 1L;
        CauseDetailsDTO mockCause = new CauseDetailsDTO();
        mockCause.setId(causeId);
        mockCause.setTitle("Test Cause");

        when(mockRestClient.get()
                .uri("/causes/{id}", causeId)
                .retrieve()
                .body(CauseDetailsDTO.class))
                .thenReturn(mockCause);

        CauseDetailsDTO result = toTest.getCauseById(causeId);

        assertNotNull(result);
        assertEquals(causeId, result.getId());
        assertEquals("Test Cause", result.getTitle());
    }

    @Test
    void testDeleteCauseFromApi_ShouldCallRestClient() {
        Long causeId = 1L;

        RestClient.RequestHeadersUriSpec mockRequest = mock(RestClient.RequestHeadersUriSpec.class);
        when(mockRestClient.delete()).thenReturn(mockRequest);
        when(mockRequest.uri(anyString(), anyLong())).thenReturn(mock(RestClient.RequestHeadersUriSpec.class));

        toTest.deleteCauseFromApi(causeId);

        verify(mockRestClient).delete();
        verify(mockRequest).uri("/causes/{id}", causeId);
    }
}
