package com.volunteerplatform.service.dtos;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentApiDTO {
   private Long id;
    private String content;
    private String authorName;
}