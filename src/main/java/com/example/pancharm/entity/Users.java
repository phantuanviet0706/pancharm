package com.example.pancharm.entity;

import com.example.pancharm.constant.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Users extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Column(length = 63, unique = true)
	String username;

	String password;

	@Column(unique = true)
	String email;

	@Column(length = 100)
	String fullname;

	LocalDate dob;

	@Column(columnDefinition = "TEXT")
	String avatar;

	@Column(columnDefinition = "TEXT")
	String address;

	@Column(length = 63)
	String phone;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	UserStatus status = UserStatus.PENDING;

	@Builder.Default
	@Column(name = "soft_deleted")
	short softDeleted = 0;

	@ManyToMany
	Set<Roles> roles;

	@OneToMany(mappedBy = "personInCharge", fetch = FetchType.LAZY)
	Set<CompanyInfos> companyInfos;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	Set<ShippingAddresses> shippingAddresses;
}
