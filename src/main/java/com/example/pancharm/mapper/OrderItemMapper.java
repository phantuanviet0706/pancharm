package com.example.pancharm.mapper;

import org.mapstruct.Mapper;

import com.example.pancharm.dto.request.order.OrderItemRequest;
import com.example.pancharm.dto.response.order.OrderItemResponse;
import com.example.pancharm.entity.OrderItems;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItems toOrderItems(OrderItemRequest request);

    OrderItemResponse toOrderItemResponse(OrderItems orderItems);
}
