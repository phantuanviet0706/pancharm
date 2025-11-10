package com.example.pancharm.dto.request.collection;

import jakarta.validation.constraints.NotBlank;

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
    String name;

    String slug;

    String description;
}
