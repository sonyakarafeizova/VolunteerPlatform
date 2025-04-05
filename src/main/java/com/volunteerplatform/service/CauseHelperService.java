package com.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.model.Cause;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service

public class CauseHelperService {
    private final CauseRepository causeRepository;

    private final RestClient causeRestClient;
    private final ModelMapper modelMapper;

    public CauseHelperService(@Qualifier("causesRestClient") RestClient causeRestClient,
                              CauseRepository causeRepository,
                              ModelMapper modelMapper) {
        this.causeRepository = causeRepository;
        this.causeRestClient = causeRestClient;
        this.modelMapper = modelMapper;
    }

    public Cause getCauseDetailsById(Long id) {
        return causeRepository.findById(id)
                .orElseGet(() -> {
                    Cause externalCause = causeRestClient
                            .get()
                            .uri("/causes/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .body(Cause.class);

                    if (externalCause == null) {
                        throw new RuntimeException("Cause with ID " + id + " not found in external API.");
                    }

                    return causeRepository.save(externalCause);
                });

    }

//    public Cause getByIdOrFetch(Long id) {
//        return causeRepository.findById(id).orElseGet(() -> fetchAndSaveCause(id));
//    }
//
//
//    public CauseDetailsDTO fetchAndSaveCause(Long id) {
//
//        CauseDetailsDTO causeDetails = causeRestClient.get()
//                .uri("/causes/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(CauseDetailsDTO.class);
//
//        if (causeDetails == null) {
//            throw new CauseNotFoundException("Cause with id " + id + " not found in API");
//        }
//
//
//        Cause cause = modelMapper.map(causeDetails, Cause.class);
//        return causeRepository.save(cause);
//    }
}