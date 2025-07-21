package com.example.pancharm.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
	@Size(min = 3, message = "Username must be at least 3 characters")
	String username;

	@Size(min = 8, message = "Password must be at least 8 characters")
	String password;

	@Email
	String email;

	String fullname;

	LocalDate dob;

	String avatar;

	String address;

	String phone;
}
