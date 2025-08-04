package com.example.pancharm.dto.response.collection;

import java.util.Set;

import com.example.pancharm.dto.response.product.ProductListResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionDetailResponse {
    int id;
    String name;
    String slug;
    String description;
    Set<CollectionImageResponse> collectionImages;
    Set<ProductListResponse> products;
}
