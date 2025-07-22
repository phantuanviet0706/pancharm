package com.example.pancharm.dto.request.role;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
	@NotNull(message = "Role name cannot be empty")
	String name;
	String description;
	Set<String> permissions;
}
