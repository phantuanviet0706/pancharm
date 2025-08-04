package com.example.pancharm.dto.request.user;

import com.example.pancharm.validator.annotation.DobConstraint;
import com.example.pancharm.validator.annotation.PhoneConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @NotBlank(message = "PASSWORD_EMPTY")
    @Size(min = 8, message = "PASSWORD_SIZE_ERROR")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$", message = "PASSWORD_PATTERN_ERROR")
    String password;

    @Email
    String email;

    String fullname;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

    String avatar;

    String address;

    @PhoneConstraint
    String phone;

    Set<String> roles;
}
