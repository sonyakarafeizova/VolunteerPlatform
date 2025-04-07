package com.volunteerplatform.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CreateCommentDTO {
    private Long authorId;
    private Long mentoringId;
    private String content;
    private String author;
    private Instant created;
}