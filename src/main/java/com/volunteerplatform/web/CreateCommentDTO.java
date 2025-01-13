package com.volunteerplatform.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentDTO {
    private Long causeId;
    private String message;
}