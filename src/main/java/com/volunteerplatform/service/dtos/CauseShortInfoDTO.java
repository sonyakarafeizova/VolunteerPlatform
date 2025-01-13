package com.volunteerplatform.service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CauseShortInfoDTO {
    private long id;
    private String name;
    private String description;
    private String imageUrl;
}
