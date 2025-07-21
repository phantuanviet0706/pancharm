package com.example.pancharm.mapper;

import com.example.pancharm.dto.request.permission.PermissionRequest;
import com.example.pancharm.dto.response.PermissionResponse;
import com.example.pancharm.entity.Permissions;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
	Permissions toPermission(PermissionRequest request);

	PermissionResponse toPermissionResponse(Permissions permission);

	void updatePermission(@MappingTarget Permissions permission, PermissionRequest request);
}
