package com.example.pancharm.dto.response.collection;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionListResponse {
    int id;
    String name;
    String slug;
    short isDefault;
    String description;
    Set<CollectionImageResponse> collectionImages;
    Set<Integer> productIds;
}
