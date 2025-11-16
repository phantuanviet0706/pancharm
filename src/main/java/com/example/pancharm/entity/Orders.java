package com.example.pancharm.entity;

import java.util.Set;

import jakarta.persistence.*;

import com.example.pancharm.constant.OrderStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 63)
    String slug;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    OrderStatus status = OrderStatus.DRAFTING;

    @Column(name = "total_price")
    float totalPrice;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(columnDefinition = "TEXT")
    String config;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id", nullable = false)
    ShippingAddresses shippingAddress;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    Set<OrderItems> orderItems;

    @OneToOne(mappedBy = "order")
    Payments payment;
}
