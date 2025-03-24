package com.volunteerplatform.service.dtos;

import com.volunteerplatform.model.Level;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CauseShortInfoDTO {
    private long id;
    private String title;
    private String description;
    private String imageUrl;
    private Level level;
}
