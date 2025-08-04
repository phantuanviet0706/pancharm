package com.example.pancharm.dto.request.collection;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionCreationRequest  {
	@NotBlank(message = "COLLECTION_NAME_REQUIRE")
	String name;

	String slug;

	String description;

	Set<MultipartFile> collectionImages;
}
