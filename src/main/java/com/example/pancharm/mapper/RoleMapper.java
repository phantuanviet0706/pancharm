package com.example.pancharm.mapper;

import com.example.pancharm.dto.request.role.RoleRequest;
import com.example.pancharm.dto.response.RoleResponse;
import com.example.pancharm.entity.Roles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	@Mapping(target = "permissions", ignore = true)
	Roles toRoles(RoleRequest request);

	RoleResponse toRoleResponse(Roles role);

	@Mapping(target = "permissions", ignore = true)
	void updateRole(@MappingTarget Roles role, RoleRequest request);
}
