package com.example.pancharm.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
    @NotBlank(message = "CATEGORY_NAME_REQUIRED")
    @Size(max = 255, message = "CATEGORY_NAME_MAX_255")
    String name;

    @Size(max = 63, message = "CATEGORY_SLUG_MAX_63")
    String slug;

    int parentId;
}
