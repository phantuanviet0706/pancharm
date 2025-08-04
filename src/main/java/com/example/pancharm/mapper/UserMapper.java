package com.example.pancharm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.pancharm.dto.request.user.*;
import com.example.pancharm.dto.response.user.*;
import com.example.pancharm.entity.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    Users toUsers(UserCreationRequest request);

    UserDetailResponse toUserResponse(Users user);

    UserListResponse toUserListResponse(Users user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(@MappingTarget Users user, UserUpdateRequest request);
}
