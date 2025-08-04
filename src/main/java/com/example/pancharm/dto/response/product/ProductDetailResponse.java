package com.example.pancharm.dto.response.product;

import java.util.Set;

import com.example.pancharm.dto.response.category.CategoryDetailResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    String name;
    String slug;
    int quantity;
    int unitPrice;
    String color;
    String status;
    String description;
    CategoryDetailResponse category;
    Set<ProductImageResponse> productImages;
}
