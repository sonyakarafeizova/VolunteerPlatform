package com.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.data.PictureRepository;
import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.dtos.CauseDetailsCommentDTO;
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
//@RequiredArgsConstructor
public class CauseService {
    private final RestClient causeRestClient;
    private Logger LOGGER = LoggerFactory.getLogger(CauseService.class);
    private final CauseRepository causeRepository;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;
    private final PictureRepository pictureRepository;
    private final Random random = new Random();

   // private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

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




//    @Transactional
//    public List<CauseShortInfoDTO> getAllCauses() {
//        return causeRepository.findAll()
//                .stream()
//                .map(this::mapToShortInfo)
//                .toList();
//    }
//
//    @Transactional
//    public CauseShortInfoDTO getRandomCause() {
//        long causeCount = causeRepository.count();
//        long randomId = random.nextInt((int) causeCount) + 1;
//        Optional<Cause> cause = causeRepository.findById(randomId);
//        if (cause.isEmpty()) {
//            throw new RuntimeException("Cause not found");
//        }
//        return mapToShortInfo(cause.get());
//    }
//
//    private CauseShortInfoDTO mapToShortInfo(Cause cause) {
//        CauseShortInfoDTO dto = modelMapper.map(cause, CauseShortInfoDTO.class);
//
//        Optional<Picture> first = cause.getPictures().stream().findFirst();
//        first.ifPresent(pic -> dto.setImageUrl(pic.getUrl()));
//        return dto;
//    }
//
//
//    public Cause getCauseById(Long id) {
//        Optional<Cause> cause = causeRepository.findById(id);
//        return cause.orElse(null);
//    }
//
//    public void deleteCause(Long id) {
//        causeRepository.deleteById(id);
//    }
//
//
//    public boolean add(AddCauseDTO data,MultipartFile file) throws IOException {
//        Cause toInsert = modelMapper.map(data, Cause.class);
//        // toInsert.setVideoUrl(YoutubeLinkConverter.convert(data.getVideoUrl()));
//        toInsert.setAuthor(userHelperService.getUser());
//        toInsert.setCreated(LocalDateTime.now());
//
//        Cause savedCause = causeRepository.save(toInsert);
//
//        if (file != null && !file.isEmpty()) {
//            String externalImageUrl=uploadFileToExternalApi(file);
//
//            Picture picture = new Picture();
//            picture.setUrl(toInsert.getImageUrl());
//            picture.setAuthor(userHelperService.getUser());
//            picture.setCause(savedCause);
//            pictureRepository.save(picture);
//        }
//        return true;
//    }


    @Transactional(readOnly = true)
    public CauseDetailsDTO getDetails(Long id) {
        Cause cause = causeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cause with id " + id + " not found"));

        CauseDetailsDTO dto = modelMapper.map(cause, CauseDetailsDTO.class);

        dto.setCreated(cause.getCreated());
        dto.setComments(cause.getComments().stream()
                .map(comment -> modelMapper.map(comment, CauseDetailsCommentDTO.class))
                .toList());


        return dto;
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