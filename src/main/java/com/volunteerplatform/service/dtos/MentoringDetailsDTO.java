package com.volunteerplatform.service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MentoringDetailsDTO {
    private long id;
    private String name;
    private String description;
    private String authorName;
    private List<MentoringDetailsCommentDTO> comments;
}
