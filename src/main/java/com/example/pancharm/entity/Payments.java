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
public class Payments extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Column(length = 63)
	String payment_method;

	float amount;

	int status;

	int paid_at;

	@Column(length = 100)
	String transaction_code;

	String resource_path;

	@OneToOne
	@JoinColumn(name = "order_id", unique = true)
	Orders order;
}
