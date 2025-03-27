package com.volunteerplatform.web.dto;

import com.volunteerplatform.model.enums.Level;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddCauseDTO {
    private String title;
    private String description;
    private Level level;
    private String videoUrl;
   private String imageUrl;
}
