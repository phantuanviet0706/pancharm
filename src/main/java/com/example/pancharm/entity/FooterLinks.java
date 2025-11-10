package com.example.pancharm.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FooterLinks extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(columnDefinition = "NVARCHAR(255)")
    String name;

    @Column(length = 63, unique = true)
    String code;

    @Column(columnDefinition = "TEXT")
    String content;

    @Column(name = "group_id")
    int groupId;

    @Builder.Default
    @Column(name = "is_active")
    boolean isActive = true;
}
