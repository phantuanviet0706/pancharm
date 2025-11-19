package com.example.pancharm.service.order;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.constant.ProductStatus;
import com.example.pancharm.dto.request.order.OrderCreationRequest;
import com.example.pancharm.dto.request.order.OrderFilterRequest;
import com.example.pancharm.dto.request.order.OrderItemRequest;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.order.OrderResponse;
import com.example.pancharm.entity.Orders;
import com.example.pancharm.entity.Products;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.OrderMapper;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.repository.OrderRepository;
import com.example.pancharm.repository.ProductRepository;
import com.example.pancharm.service.shipping.ShippingAddressService;
import com.example.pancharm.util.GeneralUtil;
import com.example.pancharm.util.PageRequestUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;
    ShippingAddressService shippingAddressService;
    OrderItemService orderItemService;
    ProductRepository productRepository;

    GeneralUtil generalUtil;
    PageMapper pageMapper;

    @Transactional
    public OrderResponse createOrder(OrderCreationRequest request) {
        Set<OrderItemRequest> itemRequests = request.getItems();
        if (itemRequests == null || itemRequests.isEmpty()) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }

        Set<Integer> productIds =
                itemRequests.stream().map(OrderItemRequest::getProductId).collect(Collectors.toSet());

        List<Products> products = productRepository.findByIdIn(productIds);

        Map<Integer, Products> productMap = products.stream().collect(Collectors.toMap(Products::getId, p -> p));

        for (OrderItemRequest itemReq : itemRequests) {
            int productId = itemReq.getProductId();
            int qty = itemReq.getQuantity();

            Products product = productMap.get(productId);

            if (product == null) {
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            }

            if (Boolean.TRUE.equals(product.getSoftDeleted())) {
                throw new AppException(ErrorCode.PRODUCT_DELETED);
            }

            // 4. Trạng thái sản phẩm
            if (product.getStatus() == ProductStatus.INACTIVE
                    || product.getStatus() == ProductStatus.COMING_SOON
                    || product.getStatus() == ProductStatus.OUT_OF_STOCK) {

                throw new AppException(ErrorCode.PRODUCT_INVALID_STATUS);
            }

            Integer stock = product.getQuantity();
            if (stock == null || stock < qty) {
                throw new AppException(ErrorCode.PRODUCT_OUT_OF_STOCK);
            }
        }

        for (OrderItemRequest itemReq : itemRequests) {
            Products product = productMap.get(itemReq.getProductId());
            product.setQuantity(product.getQuantity() - itemReq.getQuantity());
        }
        Orders order = orderMapper.toOrders(request);

        var shippingAddressReq = request.getShippingAddress();
        order.setShippingAddress(shippingAddressService.createSimpleData(shippingAddressReq));

        try {
            order = orderRepository.save(order);
        } catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        order.setSlug(generalUtil.generateSlug("order", order.getId()));
        try {
            order = orderRepository.save(order);
        } catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        order.setOrderItems(orderItemService.createListObject(itemRequests, order));

        return orderMapper.toOrderResponse(order);
    }

    public PageResponse<OrderResponse> getOrders(OrderFilterRequest request) {
        Pageable pageable = PageRequestUtil.from(request);

        Specification<Orders> spec = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("slug").as(String.class), "%" + request.getSlug() + "%"));
        }

        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getKeyword() + "%"));
        }

        return pageMapper.toPageResponse(orderRepository.findAll(spec, pageable).map(orderMapper::toOrderResponse));
    }
}
