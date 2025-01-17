package com.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.data.PictureRepository;
import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.Picture;
import com.volunteerplatform.service.dtos.CauseDetailsCommentDTO;
import com.volunteerplatform.service.dtos.CauseDetailsDTO;
import com.volunteerplatform.service.dtos.CauseShortInfoDTO;
import com.volunteerplatform.web.dto.AddCauseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CauseService {

    private final CauseRepository causeRepository;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;
    private final PictureRepository pictureRepository;
    private final Random random = new Random();

    @Transactional
    public List<CauseShortInfoDTO> getAll() {
        return causeRepository.findAll()
                .stream()
                .map(this::mapToShortInfo)
                .toList();
    }

    @Transactional
    public CauseShortInfoDTO getRandomCause() {
       long causeCount = causeRepository.count();
       long randomId = random.nextInt((int) causeCount) + 1;
       Optional<Cause> cause = causeRepository.findById(randomId);
       if(cause.isEmpty()) {
           throw new RuntimeException("Cause not found");
       }
       return mapToShortInfo(cause.get());
    }

    private CauseShortInfoDTO mapToShortInfo(Cause cause) {
        CauseShortInfoDTO dto=modelMapper.map(cause,CauseShortInfoDTO.class);

        Optional<Picture>  first=cause.getPictures().stream().findFirst();
        first.ifPresent(pic->dto.setImageUrl(pic.getUrl()));
        return dto;
    }


    public Cause createCause(Cause cause) {
        return causeRepository.save(cause);
    }

    public List<CauseShortInfoDTO> getAllCauses() {
        return causeRepository.findAll()
                .stream()
                .map(this::mapToShortInfo)
                .toList();
    }

    public Cause getCauseById(Long id) {
        Optional<Cause> cause = causeRepository.findById(id);
        return cause.orElse(null);
    }

    public void deleteCause(Long id) {
        causeRepository.deleteById(id);
    }


    public void add(AddCauseDTO data, MultipartFile file) { 
    }


    @Transactional(readOnly = true)
    public CauseDetailsDTO getDetails(Long id) {
        Cause cause = causeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cause with id " + id + " not found"));

        CauseDetailsDTO dto = modelMapper.map(cause, CauseDetailsDTO.class);
        dto.setComments(cause.getComments().stream()
                .map(comment -> modelMapper.map(comment, CauseDetailsCommentDTO.class))
                .toList());


        return dto;
    }
}