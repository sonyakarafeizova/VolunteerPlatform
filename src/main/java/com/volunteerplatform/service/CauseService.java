package com.volunteerplatform.service;

import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.data.PictureRepository;
import com.volunteerplatform.model.Cause;
import com.volunteerplatform.model.Picture;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.dtos.CauseDetailsCommentDTO;
import com.volunteerplatform.service.dtos.CauseDetailsDTO;
import com.volunteerplatform.service.dtos.CauseShortInfoDTO;
import com.volunteerplatform.web.dto.AddCauseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CauseService {

    private final CauseRepository causeRepository;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;
    private final PictureRepository pictureRepository;

    private final Random random = new Random();

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Transactional
    public List<CauseShortInfoDTO> getAllCauses() {
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
        if (cause.isEmpty()) {
            throw new RuntimeException("Cause not found");
        }
        return mapToShortInfo(cause.get());
    }

    private CauseShortInfoDTO mapToShortInfo(Cause cause) {
        CauseShortInfoDTO dto = modelMapper.map(cause, CauseShortInfoDTO.class);

        Optional<Picture> first = cause.getPictures().stream().findFirst();
        first.ifPresent(pic -> dto.setImageUrl(pic.getUrl()));
        return dto;
    }


    public Cause getCauseById(Long id) {
        Optional<Cause> cause = causeRepository.findById(id);
        return cause.orElse(null);
    }

    public void deleteCause(Long id) {
        causeRepository.deleteById(id);
    }


    public boolean add(AddCauseDTO data, MultipartFile file) throws IOException {
        Cause toInsert = modelMapper.map(data, Cause.class);
        // toInsert.setVideoUrl(YoutubeLinkConverter.convert(data.getVideoUrl()));
        toInsert.setAuthor(userHelperService.getUser());

        if (file != null && !file.isEmpty()) {
            String imageUrl = saveFile(file);
            toInsert.setImageUrl(imageUrl);
        }
        toInsert.setCreated(LocalDateTime.now());
        Cause savedCause = causeRepository.save(toInsert);

        if (file != null && !file.isEmpty()) {

            Picture picture = new Picture();
            picture.setUrl(toInsert.getImageUrl());
            picture.setAuthor(userHelperService.getUser());
            picture.setCause(savedCause);
            pictureRepository.save(picture);
        }
        return true;
    }

    private String saveFile(MultipartFile file) throws IOException {

//        Path uploadPath = Paths.get(UPLOAD_DIR);
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        Path filePath = uploadPath.resolve(fileName);
//
//
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("uploads/" + fileName);

        Files.createDirectories(filePath.getParent()); // Ensure directory exists
        Files.write(filePath, file.getBytes()); // Save the file


        return "/uploads/" + fileName;
    }


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