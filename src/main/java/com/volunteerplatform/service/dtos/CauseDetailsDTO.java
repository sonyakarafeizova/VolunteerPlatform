package com.volunteerplatform.service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class CauseDetailsDTO {

    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private String authorName;
    private LocalDateTime created;
    private List<String> imageUrl;
    private List<CauseDetailsCommentDTO> comments;



}
