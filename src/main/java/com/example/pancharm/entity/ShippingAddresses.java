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

	@Column(name = "recipient_name", length = 100)
	String recipientName;

	@Column(columnDefinition = "TEXT")
	String address;

	@Column(length = 100)
	String ward;

	@Column(length = 100)
	String district;

	@Column(length = 100)
	String province;

	@Column(name = "phone_number", length = 35)
	String phoneNumber;

	@Column(name = "zip_code", length = 100)
	String zipCode;

	@Builder.Default
	@Column(name = "is_default")
	short isDefault = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	Users user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	Orders order;
}
