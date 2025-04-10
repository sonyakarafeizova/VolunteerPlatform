package com.volunteerplatform.web.dto;

import com.volunteerplatform.model.enums.Level;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddCauseDTO {
    @NotBlank(message = "Title cannot be blank.")
    @Size(min = 3, message = "Title must be at least 3 characters.")
    private String title;

    @NotBlank(message = "Description cannot be blank.")
    @Size(min = 10, message = "Description must be at least 10 characters.")
    private String description;
    private Level level;
    private String videoUrl;
   private String imageUrl;
}
