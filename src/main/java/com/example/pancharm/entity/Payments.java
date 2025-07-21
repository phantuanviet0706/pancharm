package com.example.pancharm.entity;

import com.example.pancharm.constant.PaymentMethodStatus;
import com.example.pancharm.constant.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;

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

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", length = 63, nullable = false)
	PaymentMethodStatus paymentMethod = PaymentMethodStatus.CASH;

	float amount;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	PaymentStatus status = PaymentStatus.PENDING;

	@Column(name = "paid_at")
	int paidAt;

	@Column(name = "transaction_code", length = 100)
	String transactionCode;

	@Column(name = "resource_path")
	String resourcePath;

	@OneToOne
	@JoinColumn(name = "order_id", unique = true)
	Orders order;
}
