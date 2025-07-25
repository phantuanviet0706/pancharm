package com.example.pancharm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.pancharm.dto.request.auth.RegisterRequest;
import com.example.pancharm.dto.response.RegisterResponse;
import com.example.pancharm.entity.Users;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    @Mapping(target = "fullname", ignore = true)
    Users toRegisterUser(RegisterRequest registerRequest);

    RegisterResponse toRegisterResponse(Users users);
}
