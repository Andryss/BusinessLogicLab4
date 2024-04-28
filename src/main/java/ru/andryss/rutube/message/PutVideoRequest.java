package ru.andryss.rutube.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.andryss.rutube.model.VideoAccess;
import ru.andryss.rutube.model.VideoCategory;

@Data
public class PutVideoRequest {
    @NotBlank
    @Size(min = 1, max = 100)
    String title;
    @NotNull
    @Size(max = 5000)
    String description;
    @NotNull
    VideoCategory category;
    @NotNull
    VideoAccess access;
    boolean ageRestriction = false;
    boolean comments = true;
}
