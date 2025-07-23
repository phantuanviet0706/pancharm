package com.example.pancharm.dto.request.company;

import com.example.pancharm.entity.Company;
import com.example.pancharm.entity.Users;
import com.example.pancharm.validator.annotation.PhoneConstraint;
import jakarta.validation.constraints.Email;
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
	String username;
	String companyName;
}
