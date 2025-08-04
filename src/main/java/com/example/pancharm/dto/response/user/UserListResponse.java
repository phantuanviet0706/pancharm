package com.example.pancharm.dto.response.user;

import com.example.pancharm.constant.UserStatus;
import com.example.pancharm.dto.response.role.RoleResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserListResponse {
	int id;
	String username;
	String email;
	String fullname;
	String avatar;
	String address;
	String phone;
	UserStatus status;
}
