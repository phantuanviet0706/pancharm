package com.example.pancharm.dto.request.user;

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
public class UserChangePassRequest {
    @NotBlank(message = "PASSWORD_EMPTY")
    @Size(min = 8, message = "PASSWORD_SIDE_ERROR")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$", message = "PASSWORD_PATTERN_ERROR")
    String password;

    @NotBlank(message = "PASSWORD_EMPTY")
    @Size(min = 8, message = "PASSWORD_SIDE_ERROR")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$", message = "PASSWORD_PATTERN_ERROR")
    String confirmPassword;
}
