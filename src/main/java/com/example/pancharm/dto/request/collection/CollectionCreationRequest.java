package com.example.pancharm.dto.request.collection;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionCreationRequest {
    @NotBlank(message = "COLLECTION_NAME_REQUIRE")
    String name;

    String slug;

    String description;

    Set<MultipartFile> collectionImages;
}
