package com.example.pancharm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.pancharm.dto.request.user.UserRequest;
import com.example.pancharm.dto.response.user.UserResponse;
import com.example.pancharm.entity.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    Users toUsers(UserRequest request);

    UserResponse toUserResponse(Users user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(@MappingTarget Users user, UserRequest request);
}
