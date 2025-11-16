package com.example.pancharm.dto.response.order;

import com.example.pancharm.dto.response.product.ProductSimpleResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    int id;
    float unitPrice;
    int quantity;
    ProductSimpleResponse product;
}
