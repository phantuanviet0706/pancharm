package com.example.pancharm.dto.response;

import com.example.pancharm.constant.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
	String id;
	String username;
	String email;
	String fullname;
	LocalDate dob;
	String avatar;
	String address;
	String phone;
	UserStatus status;
	short softDeleted;
	Set<RoleResponse> roles;
//	Set<CompanyInfos> companyInfos;
//	Set<ShippingAddresses>  shippingAddresses;
}
