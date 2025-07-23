package com.example.pancharm.dto.request.company;

import com.example.pancharm.entity.Company;
import com.example.pancharm.entity.Users;
import com.example.pancharm.validator.annotation.PhoneConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyInfoRequest {
	String address;

	@PhoneConstraint
	String phone;

	@Email
	String email;

	@NotNull(message = "USER_NOT_NULL")
	@Valid
	Users user;

	@NotNull(message = "COMPANY_NOT_NULL")
	@Valid
	Company company;
}
