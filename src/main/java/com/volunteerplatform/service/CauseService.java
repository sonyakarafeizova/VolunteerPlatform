package com.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.data.PictureRepository;
import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.dtos.CauseDetailsDTO;
import com.volunteerplatform.service.dtos.CauseShortInfoDTO;
import com.volunteerplatform.web.dto.AddCauseDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class CauseService {
    private final RestClient causeRestClient;
    private Logger LOGGER = LoggerFactory.getLogger(CauseService.class);
    private final CauseRepository causeRepository;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;
    private final PictureRepository pictureRepository;
    private UserRepository userRepository;
    private final Random random = new Random();

    public CauseService(@Qualifier("causesRestClient") RestClient restClient,
                         CauseRepository causeRepository,
                        ModelMapper modelMapper,
                        UserHelperService userHelperService,
                        PictureRepository pictureRepository) {

        this.causeRestClient = restClient;
        this.causeRepository = causeRepository;
        this.modelMapper = modelMapper;
        this.userHelperService = userHelperService;
        this.pictureRepository = pictureRepository;
    }


    public List<CauseShortInfoDTO> getAllCauses() {
        return causeRestClient.get()
                .uri("/causes")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }



    public CauseDetailsDTO getCauseById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Cause ID cannot be null");
        }

        return causeRestClient.get()
                .uri("/causes/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(CauseDetailsDTO.class);
    }


    public void createCause(AddCauseDTO addCauseDTO) {
        LOGGER.debug("Creating new cause...");

        causeRestClient.post()
                .uri("/causes")
                .body(addCauseDTO)
                .retrieve();

    }


    public void deleteCauseFromApi(Long id) {
        causeRestClient.delete()
                .uri("/causes/{id}", id)
                .retrieve();
    }



    @Transactional(readOnly = true)
    public CauseDetailsDTO getCauseDetails(Long id) {

        return causeRestClient.get()
                .uri("/causes/{id}")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(CauseDetailsDTO.class);

    }

    public List<Cause> findByUser(User user) {
        List<Cause> causes = causeRepository.findByAuthor(user);
        if (causes.isEmpty()) {
            return Collections.emptyList();
        }
        return causes;
    }

    @Transactional
    public int removeCausesOlderThanMonths(int months) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusMonths(months);
        return causeRepository.deleteByCreatedBefore(thresholdDate);
    }




}