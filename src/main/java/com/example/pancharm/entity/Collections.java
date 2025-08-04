package com.example.pancharm.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import com.example.pancharm.constant.CollectionStatus;

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
public class Collections extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    @Column(length = 63, unique = true)
    String slug;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    CollectionStatus status = CollectionStatus.DRAFTING;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(columnDefinition = "TEXT")
    String config;

    @OneToMany(mappedBy = "collection", fetch = FetchType.LAZY)
    Set<CollectionImages> images = new HashSet<>();

    @ManyToMany(mappedBy = "collections")
    Set<Products> products = new HashSet<>();
}
