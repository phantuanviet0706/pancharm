package com.example.pancharm.entity;

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
public class ShippingAddresses extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Column(length = 100)
	String recipient_name;

	@Column(columnDefinition = "TEXT")
	String address;

	@Column(length = 100)
	String ward;

	@Column(length = 100)
	String district;

	@Column(length = 100)
	String province;

	@Column(length = 35)
	String phone_number;

	@Column(length = 100)
	String zip_code;

	short is_default;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	Users user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	Orders order;
}
