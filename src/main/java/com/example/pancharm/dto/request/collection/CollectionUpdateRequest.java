package com.example.pancharm.dto.request.collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionUpdateRequest extends CollectionImageRequest {
    @NotBlank(message = "COLLECTION_NAME_REQUIRE")
    @Size(max = 255, message = "COLLECTION_NAME_MAX_255")
    String name;

    @Size(max = 63, message = "COLLECTION_SLUG_MAX_63")
    String slug;

    String description;
}
