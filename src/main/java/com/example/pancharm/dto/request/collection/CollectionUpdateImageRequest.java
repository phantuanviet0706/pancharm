package com.example.pancharm.dto.request.collection;

import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionUpdateImageRequest {
    @NotNull(message = "DEFAULT_IMAGE_REQUIRE")
    Integer defaultImageId;
}
