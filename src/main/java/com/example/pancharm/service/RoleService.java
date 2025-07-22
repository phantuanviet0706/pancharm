package com.example.pancharm.service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.constant.PredefineRole;
import com.example.pancharm.dto.request.role.RoleRequest;
import com.example.pancharm.dto.response.RoleResponse;
import com.example.pancharm.entity.Permissions;
import com.example.pancharm.entity.Roles;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.RoleMapper;
import com.example.pancharm.repository.PermissionRepository;
import com.example.pancharm.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.security.Permission;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name())")
public class RoleService {
	RoleRepository roleRepository;
	RoleMapper roleMapper;

	PermissionRepository permissionRepository;

	static final Set<String> FIXED_ROLES = Set.of(
			PredefineRole.SUPER_ADMIN.getName(),
			PredefineRole.ADMIN.getName(),
			PredefineRole.USER.getName()
	);

	public RoleResponse createRole(RoleRequest request) {
		if (roleRepository.existsByName(request.getName())) {
			throw new AppException(ErrorCode.ROLE_EXISTED);
		}

		Roles role = roleMapper.toRoles(request);
		setPermission(role, request.getPermissions());

		try {
			role = roleRepository.save(role);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		return roleMapper.toRoleResponse(role);
	}

	public RoleResponse updateRole(RoleRequest request, int roleId) {
		Roles role =  roleRepository.findById(String.valueOf(roleId)).orElseThrow(
				() -> new AppException(ErrorCode.ROLE_NOT_FOUND)
		);

		roleMapper.updateRole(role, request);
		setPermission(role, request.getPermissions());

		try {
			role = roleRepository.save(role);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		return roleMapper.toRoleResponse(role);
	}

	public List<RoleResponse> findAll() {
		return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
	}

	public void deleteRole(int roleId) {
		if (!roleRepository.existsById(String.valueOf(roleId))) {
			return;
		}

		roleRepository.deleteById(String.valueOf(roleId));
	}

	private void setPermission(Roles role, Set<String> permissionNames) {
		var permissions = permissionRepository.findAllByNameIn(permissionNames);
		role.setPermissions(new HashSet<>(permissions));
	}
}
