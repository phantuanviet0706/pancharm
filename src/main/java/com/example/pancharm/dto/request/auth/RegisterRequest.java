package com.example.pancharm.dto.request.auth;

import com.example.pancharm.validator.annotation.PhoneConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
	@NotBlank(message = "FIRSTNAME_REQUIRED")
	String firstName;

	@NotBlank(message = "LASTNAME_REQUIRED")
	String lastName;

	@NotBlank(message = "USERNAME_EMPTY")
	@Size(min = 6, message = "USERNAME_SIZE_ERROR")
	@Pattern(regexp = "^[A-Za-z\\d]+$", message = "USERNAME_PATTERN_ERROR")
	String username;

	@NotBlank(message = "EMAIL_REQUIRED")
	@Email
	String email;

	@PhoneConstraint
	String phoneNumber;

	@NotBlank(message = "PASSWORD_EMPTY")
	@Size(min = 8, message = "PASSWORD_SIZE_ERROR")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$", message = "PASSWORD_PATTERN_ERROR")
	String password;
}
