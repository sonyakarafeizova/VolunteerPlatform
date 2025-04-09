package com.example.volunteerplatform.service;

import com.volunteerplatform.data.PictureRepository;
import com.volunteerplatform.data.CauseRepository;
import com.volunteerplatform.model.Picture;
import com.volunteerplatform.model.User;
import com.volunteerplatform.service.CauseHelperService;
import com.volunteerplatform.service.CloudinaryService;
import com.volunteerplatform.service.PictureService;
import com.volunteerplatform.service.UserHelperService;
import com.volunteerplatform.web.dto.UploadPictureDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PictureServiceTest {

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private UserHelperService userHelperService;

    @Mock
    private CauseRepository causeRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private CauseHelperService causeHelperService;

    private PictureService pictureService;

    @Captor
    private ArgumentCaptor<Picture> pictureCaptor;

    @BeforeEach
    void setUp() {
        pictureService = new PictureService(pictureRepository, userHelperService, causeRepository, cloudinaryService, causeHelperService);
    }

    @Test
    void testCreatePicture() {

        UploadPictureDTO dto = new UploadPictureDTO();
        dto.setPicture(null);
        dto.setTitle("Test Pic");
        dto.setCauseId(1L);
        String uploadedUrl = "http://example.com/image.jpg";

        User user = new User();
        user.setUsername("testUser");


        when(cloudinaryService.upload((MultipartFile) any(), eq(""))).thenReturn(uploadedUrl);
        when(causeHelperService.getCauseDetailsById(1L)).thenReturn(new com.volunteerplatform.model.Cause());
        when(userHelperService.getUser()).thenReturn(user);
        when(pictureRepository.save(any(Picture.class))).thenAnswer(invocation -> invocation.getArgument(0));


        pictureService.create(dto);


        verify(pictureRepository).save(pictureCaptor.capture());
        Picture savedPicture = pictureCaptor.getValue();
        assertEquals(uploadedUrl, savedPicture.getUrl());
        assertEquals("Test Pic", savedPicture.getTitle());
        assertEquals(user, savedPicture.getAuthor());
    }

    @Test
    void testGetPicturesByCauseId() {
        List<Picture> pictures = List.of(new Picture(), new Picture());
        when(pictureRepository.findAllByCauseId(1L)).thenReturn(pictures);
        List<Picture> result = pictureService.getPicturesByCauseId(1L);
        assertEquals(2, result.size());
    }
}
