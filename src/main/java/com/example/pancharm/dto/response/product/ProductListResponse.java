package com.example.pancharm.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductListResponse {
    int id;
    String name;
    String slug;
    int quantity;
    int unitPrice;
    String status;
    Set<ProductImageResponse> productImages;
}
