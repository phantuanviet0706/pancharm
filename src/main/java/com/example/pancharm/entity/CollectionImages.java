package com.example.pancharm.entity;

import com.example.pancharm.common.contract.ImageEntity;
import jakarta.persistence.*;

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
public class CollectionImages extends BaseEntity implements ImageEntity<Collections> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String path;

    @Builder.Default
    @Column(name = "is_default")
    short isDefault = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    Collections collection;

    @Override
    public void setParent(Collections parent) {
        this.collection = parent;
    }
}
