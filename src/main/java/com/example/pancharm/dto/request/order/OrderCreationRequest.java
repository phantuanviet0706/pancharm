package com.example.pancharm.dto.request.order;

import java.util.Set;

import com.example.pancharm.dto.request.shipping.ShippingAddressRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {
    float totalPrice;
    String description;
    ShippingAddressRequest shippingAddress;
    Set<OrderItemRequest> items;
}
