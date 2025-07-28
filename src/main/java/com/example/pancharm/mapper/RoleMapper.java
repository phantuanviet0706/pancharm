package com.example.pancharm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.pancharm.dto.request.role.RoleRequest;
import com.example.pancharm.dto.response.role.RoleResponse;
import com.example.pancharm.entity.Roles;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Roles toRoles(RoleRequest request);

    RoleResponse toRoleResponse(Roles role);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(@MappingTarget Roles role, RoleRequest request);
}
