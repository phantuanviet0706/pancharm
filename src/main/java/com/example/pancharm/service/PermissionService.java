package com.example.pancharm.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.permission.PermissionRequest;
import com.example.pancharm.dto.response.permission.PermissionResponse;
import com.example.pancharm.entity.Permissions;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.PermissionMapper;
import com.example.pancharm.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name())")
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    /**
     * @desc Create new permission
     * @param request
     * @return PermissionResponse
     */
    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        Permissions permission = permissionMapper.toPermission(request);

        try {
            permission = permissionRepository.save(permission);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return permissionMapper.toPermissionResponse(permission);
    }

    /**
     * @desc Update existing permission
     * @param request
     * @param permissionId
     * @return PermissionResponse
     */
    public PermissionResponse updatePermission(PermissionRequest request, int permissionId) {
        Permissions permission = permissionRepository
                .findById(String.valueOf(permissionId))
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        permissionMapper.updatePermission(permission, request);

        try {
            permission = permissionRepository.save(permission);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return permissionMapper.toPermissionResponse(permission);
    }

    /**
     * @desc Get all existing permissions
     * @return List<PermissionResponse>
     */
    public List<PermissionResponse> findAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    /**
     * @desc Delete existing permission
     * @param permissionId
     */
    public void deletePermission(int permissionId) {
        if (!permissionRepository.existsById(String.valueOf(permissionId))) {
            return;
        }
        permissionRepository.deleteById(String.valueOf(permissionId));
    }
}
