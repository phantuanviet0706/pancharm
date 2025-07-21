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
public class CompanyInfos extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	String address;

	@Column(length = 35)
	String phone;

	String email;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_in_charge", nullable = false)
	Users personInCharge;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", nullable = false)
	Company company;

}
