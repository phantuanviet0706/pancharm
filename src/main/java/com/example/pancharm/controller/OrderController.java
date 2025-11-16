package com.example.pancharm.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.pancharm.dto.request.order.OrderCreationRequest;
import com.example.pancharm.dto.request.order.OrderFilterRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.order.OrderResponse;
import com.example.pancharm.service.order.OrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderController {
    private OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderCreationRequest orderCreationRequest) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(orderCreationRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<OrderResponse>> getOrders(OrderFilterRequest request) {
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .result(orderService.getOrders(request))
                .build();
    }
}
