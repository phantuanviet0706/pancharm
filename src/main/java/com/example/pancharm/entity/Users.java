package com.example.pancharm.entity;

import com.example.pancharm.constant.MembershipLevel;
import com.example.pancharm.constant.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

	@NotBlank(message = "USERNAME_REQUIRED")
	@Column(length = 63, unique = true)
	String username;

	@NotBlank(message = "PASSWORD_REQUIRED")
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

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "membership_level", nullable = false)
	MembershipLevel memberShipLevel = MembershipLevel.MEMBER;

	@Column(name = "total_spent")
	int totalSpent;

	double points;

	@Column(columnDefinition = "TEXT")
	String config;

	@ManyToMany
	Set<Roles> roles;

	@OneToMany(mappedBy = "personInCharge", fetch = FetchType.LAZY)
	Set<CompanyInfos> companyInfos;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	Set<ShippingAddresses> shippingAddresses;
}
