package com.example.pancharm.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
	@Size(min = 8, message = "Password must be at least 8 characters")
	String password;

	@Email
	String email;

	String fullname;

	LocalDate dob;

	String avatar;

	String address;

	String phone;

	Set<String> roles;
}
