package com.volunteerplatform.web.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentoringCreateDTO {
    private Long id;
    private String title;
    private String description;
    private String level;

}