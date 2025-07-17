package com.example.pancharm.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

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

	int status;

	float total_price;

	@Column(columnDefinition = "TEXT")
	String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	Users user;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	Set<ShippingAddresses> shipping_addresses;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	Set<OrderItems>  order_items;

	@OneToOne(mappedBy = "order")
	Payments payment;
}
