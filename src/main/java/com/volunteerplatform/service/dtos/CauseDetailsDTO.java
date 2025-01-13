package com.volunteerplatform.service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class CauseDetailsDTO {
    private Long id;
    private String name;
    private String description;
    private String videoUrl;
    private String authorName;
    private List<String> imageUrls;
    private List<CauseDetailsCommentDTO> comments;


}
