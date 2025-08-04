package com.example.pancharm.entity;

import java.util.HashSet;
import java.util.Set;

import com.example.pancharm.common.contract.ImageAttachable;
import jakarta.persistence.*;

import com.example.pancharm.constant.ProductStatus;

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
public class Products extends BaseEntity implements ImageAttachable<ProductImages> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    @Column(length = 63)
    String slug;

    int quantity;

    @Column(name = "unit_price")
    int unitPrice;

    @Column(length = 10)
    String color;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    ProductStatus status = ProductStatus.ACTIVE;

    @Column(columnDefinition = "TEXT")
    String description;

    @Builder.Default
    @Column(name = "soft_deleted")
    short softDeleted = 0;

    @Column(columnDefinition = "TEXT")
    String config;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Categories category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    Set<ProductImages> images = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "product_collections",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "collection_id"))
    Set<Collections> collections = new HashSet<>();

    @OneToOne(mappedBy = "product")
    OrderItems orderItems;
}
