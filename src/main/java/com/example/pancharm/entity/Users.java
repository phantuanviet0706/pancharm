package com.example.pancharm.entity;

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

	@Column(length = 63)
	String username;

	String password;

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

	short status;

	String role;

	short soft_deleted;

	@ManyToMany
	Set<Roles> roles;

	@OneToMany(mappedBy = "person_in_charge", fetch = FetchType.LAZY)
	Set<CompanyInfos> company_infos;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	Set<ShippingAddresses> shipping_addresses;
}
