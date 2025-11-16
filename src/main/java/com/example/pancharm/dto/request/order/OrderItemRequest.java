package com.example.pancharm.dto.request.order;

import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest {
    @NotNull(message = "INVALID_PRODUCT_ID")
    Integer productId;

    @NotNull(message = "INVALID_QUANTITY")
    int quantity;

    float unitPrice;
}
