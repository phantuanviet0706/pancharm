package com.example.pancharm.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Products extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	String name;

	@Column(length = 63)
	String slug;

	int quantity;

	int unit_price;

	@Column(length = 10)
	String color;

	int status;

	@Column(columnDefinition = "TEXT")
	String description;

	short soft_deleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	Categories category;

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	Set<ProductImages> images = new HashSet<>();

	@ManyToMany
	@JoinTable(
			name = "product_collections",
			joinColumns = @JoinColumn(name = "product_id"),
			inverseJoinColumns = @JoinColumn(name = "collection_id")
	)
	Set<Collections> collections = new HashSet<>();

	@OneToOne(mappedBy = "product")
	OrderItems orderItems;
}
