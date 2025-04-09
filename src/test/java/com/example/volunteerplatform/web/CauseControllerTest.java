package com.example.volunteerplatform.web;

import com.volunteerplatform.VolunteerPlatformApplication;
import com.volunteerplatform.model.Picture;
import com.volunteerplatform.service.CauseService;
import com.volunteerplatform.service.CloudinaryService;
import com.volunteerplatform.service.PictureService;
import com.volunteerplatform.service.UserService;
import com.volunteerplatform.service.dtos.CauseDetailsDTO;
import com.volunteerplatform.service.dtos.CauseShortInfoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = VolunteerPlatformApplication.class)
@AutoConfigureMockMvc
@WithMockUser
class CauseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CauseService causeService;

    @MockBean
    private PictureService pictureService;

    @MockBean
    private CloudinaryService cloudinaryService;

    @MockBean
    private UserService userService;

    @Test
    void testCausesPage() throws Exception {
        List<CauseShortInfoDTO> dummyCauses = List.of(new CauseShortInfoDTO());
        when(causeService.getAllCauses()).thenReturn(dummyCauses);

        mockMvc.perform(get("/causes"))
                .andExpect(status().isOk())
                .andExpect(view().name("causes"))
                .andExpect(model().attribute("allCauses", dummyCauses));
    }

    @Test
    void testAddCausePage() throws Exception {
        mockMvc.perform(get("/add-cause"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-cause"))
                .andExpect(model().attributeExists("cause"))
                .andExpect(model().attributeExists("levels"))
                .andExpect(model().attributeExists("causeData"));
    }

    @Test
    void testGetCauseDetails() throws Exception {
        Long id = 1L;
        CauseDetailsDTO dummyDetails = new CauseDetailsDTO();
        dummyDetails.setId(id);
        dummyDetails.setTitle("Sample Cause");
        List<Picture> dummyPictures = Collections.emptyList();

        when(causeService.getCauseById(eq(id))).thenReturn(dummyDetails);
        when(pictureService.getPicturesByCauseId(eq(id))).thenReturn(dummyPictures);

        mockMvc.perform(get("/causes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("causes-details"))
                .andExpect(model().attribute("causes", dummyDetails))
                .andExpect(model().attribute("pictures", dummyPictures));
    }

    @Test
    void testDeleteCause() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/causes/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/causes"));

        verify(causeService, times(1)).deleteCauseFromApi(eq(id));
    }
}
