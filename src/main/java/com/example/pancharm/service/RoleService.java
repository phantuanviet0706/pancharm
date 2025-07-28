package com.example.pancharm.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.constant.PredefineRole;
import com.example.pancharm.dto.request.role.RoleRequest;
import com.example.pancharm.dto.response.role.RoleResponse;
import com.example.pancharm.entity.Roles;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.RoleMapper;
import com.example.pancharm.repository.PermissionRepository;
import com.example.pancharm.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name())")
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    static final Set<String> FIXED_ROLES =
            Set.of(PredefineRole.SUPER_ADMIN.getName(), PredefineRole.ADMIN.getName(), PredefineRole.USER.getName());

    /**
     * @desc Create new role
     * @param request
     * @return RoleResponse
     */
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

    /**
     * @desc Update existing role
     * @param request
     * @param roleId
     * @return RoleResponse
     */
    public RoleResponse updateRole(RoleRequest request, int roleId) {
        Roles role = roleRepository
                .findById(String.valueOf(roleId))
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        boolean containDefaultRole = FIXED_ROLES.contains(role.getName());

        if (!role.getName().equals(request.getName()) && containDefaultRole) {
            throw new AppException(ErrorCode.ROLE_UPDATION_DENIED);
        }

        roleMapper.updateRole(role, request);
        setPermission(role, request.getPermissions());

        try {
            role = roleRepository.save(role);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return roleMapper.toRoleResponse(role);
    }

    /**
     * @desc Get all existing roles
     * @return List<RoleResponse>
     */
    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    /**
     * @desc Delete existing role
     * @param roleId
     */
    public void deleteRole(int roleId) {
        var role = roleRepository
                .findById(String.valueOf(roleId))
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        if (FIXED_ROLES.contains(role.getName())) {
            throw new AppException(ErrorCode.ROLE_DELETION_DENIED);
        }

        roleRepository.deleteById(String.valueOf(roleId));
    }

    /**
     * @desc Set permissions for specific role
     * @param role
     * @param permissionNames
     */
    private void setPermission(Roles role, Set<String> permissionNames) {
        var permissions = permissionRepository.findAllByNameIn(permissionNames);
        role.setPermissions(new HashSet<>(permissions));
    }
}
