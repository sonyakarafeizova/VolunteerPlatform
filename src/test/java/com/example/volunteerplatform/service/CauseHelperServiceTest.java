package com.example.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.model.Cause;
import com.volunteerplatform.service.CauseHelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CauseHelperServiceTest {

    @Mock
    private CauseRepository causeRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private ModelMapper modelMapper;

    private CauseHelperService causeHelperService;

    @BeforeEach
    void setUp() {
        causeHelperService = new CauseHelperService(restClient, causeRepository, modelMapper);
    }

    @Test
    void testGetCauseDetailsById_WhenCauseExistsInDatabase() {

        Cause cause = new Cause();
        cause.setId(1L);
        when(causeRepository.findById(1L)).thenReturn(Optional.of(cause));

        Cause result = causeHelperService.getCauseDetailsById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(restClient, never()).get();
    }

    @Test
    void testGetCauseDetailsById_WhenCauseNotFoundInDatabase() {

        when(causeRepository.findById(2L)).thenReturn(Optional.empty());

        RestClient.RequestHeadersUriSpec requestSpec;
        requestSpec = (RestClient.RequestHeadersUriSpec) mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        Cause externalCause = new Cause();
        externalCause.setId(2L);
        externalCause.setTitle("External Cause");

        when(restClient.get()).thenReturn(requestSpec);
        when(requestSpec.uri(eq("/causes/{id}"), eq(2L))).thenReturn(requestSpec);
        when(requestSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(eq(Cause.class))).thenReturn(externalCause);
        when(causeRepository.save(any(Cause.class))).thenReturn(externalCause);

        Cause result = causeHelperService.getCauseDetailsById(2L);

        assertNotNull(result);
        assertEquals(2L, result.getId());
    }
}
