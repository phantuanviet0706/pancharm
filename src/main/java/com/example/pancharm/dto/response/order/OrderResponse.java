package com.example.pancharm.dto.response.order;

import java.util.Set;

import com.example.pancharm.constant.OrderStatus;
import com.example.pancharm.dto.response.shipping.ShippingAddressResponse;
import com.example.pancharm.entity.Users;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    int id;
    OrderStatus status;
    String description;
    float totalPrice;
    Users user;
    String slug;
    ShippingAddressResponse shippingAddress;
    Set<OrderItemResponse> orderItems;
}
