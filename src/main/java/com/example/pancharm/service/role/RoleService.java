package com.example.pancharm.service.role;

import java.util.HashSet;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.constant.PredefineRole;
import com.example.pancharm.dto.request.role.RoleFilterRequest;
import com.example.pancharm.dto.request.role.RoleRequest;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.role.RoleResponse;
import com.example.pancharm.entity.Roles;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.mapper.RoleMapper;
import com.example.pancharm.repository.PermissionRepository;
import com.example.pancharm.repository.RoleRepository;
import com.example.pancharm.util.PageRequestUtil;

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
    PageMapper pageMapper;

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
     * @param request
     * @return PageResponse<RoleResponse>
     */
    public PageResponse<RoleResponse> findAll(RoleFilterRequest request) {
        Pageable pageable = PageRequestUtil.from(request);

        Specification<Roles> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getKeyword() + "%"));
        }

        return pageMapper.toPageResponse(roleRepository.findAll(spec, pageable).map(roleMapper::toRoleResponse));
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
