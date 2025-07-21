package com.example.pancharm.mapper;

import com.example.pancharm.dto.request.user.UserCreationRequest;
import com.example.pancharm.dto.request.user.UserUpdateRequest;
import com.example.pancharm.dto.response.UserResponse;
import com.example.pancharm.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
	Users toUsers(UserCreationRequest request);

	UserResponse toUserResponse(Users user);

	@Mapping(target = "roles", ignore = true)
	void updateUser(@MappingTarget Users user, UserUpdateRequest request);
}
