package com.example.pancharm.mapper;

import org.mapstruct.Mapper;

import com.example.pancharm.dto.request.order.OrderCreationRequest;
import com.example.pancharm.dto.response.order.OrderResponse;
import com.example.pancharm.entity.Orders;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Orders toOrders(OrderCreationRequest request);

    OrderResponse toOrderResponse(Orders orders);
}
