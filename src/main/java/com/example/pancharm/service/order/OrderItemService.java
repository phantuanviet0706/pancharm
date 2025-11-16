package com.example.pancharm.service.order;

import java.util.HashSet;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.order.OrderItemRequest;
import com.example.pancharm.entity.OrderItems;
import com.example.pancharm.entity.Orders;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.OrderItemMapper;
import com.example.pancharm.repository.OrderItemRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemService {
    OrderItemRepository orderItemRepository;
    OrderItemMapper orderItemMapper;

    public Set<OrderItems> createListObject(Set<OrderItemRequest> request, Orders order) {
        var orderItems = new HashSet<OrderItems>();

        for (OrderItemRequest orderItemRequest : request) {
            OrderItems orderItem = orderItemMapper.toOrderItems(orderItemRequest);
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        try {
            orderItems = new HashSet<>(orderItemRepository.saveAll(orderItems));
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return orderItems;
    }
}