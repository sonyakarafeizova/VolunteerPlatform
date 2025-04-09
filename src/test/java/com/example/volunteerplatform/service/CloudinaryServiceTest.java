package com.example.volunteerplatform.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.volunteerplatform.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = com.volunteerplatform.VolunteerPlatformApplication.class)
public class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    private CloudinaryService cloudinaryService;

    @Mock
    private Uploader uploader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cloudinaryService = new CloudinaryService(cloudinary);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void testUploadByteArraySuccess() throws IOException {
        byte[] fileBytes = "test data".getBytes();
        Map<String, Object> response = new HashMap<>();
        response.put("secure_url", "http://example.com/image.jpg");
        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(response);

        String url = cloudinaryService.upload(fileBytes, "folder");

        assertEquals("http://example.com/image.jpg", url);
    }
}
