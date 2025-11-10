package com.example.pancharm.service.permission;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.permission.PermissionFilterRequest;
import com.example.pancharm.dto.request.permission.PermissionRequest;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.permission.PermissionResponse;
import com.example.pancharm.entity.Permissions;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.mapper.PermissionMapper;
import com.example.pancharm.repository.PermissionRepository;
import com.example.pancharm.util.GeneralUtil;
import com.example.pancharm.util.PageRequestUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name())")
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    PageMapper pageMapper;
    GeneralUtil generalUtil;

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
     * @param request
     * @return PageResponse<PermissionResponse>
     */
    public PageResponse<PermissionResponse> findAll(PermissionFilterRequest request) {
        Pageable pageable = PageRequestUtil.from(request);

        Specification<Permissions> spec = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            spec = spec.and(
                    (root, query, cb) -> cb.like(root.get("name").as(String.class), "%" + request.getKeyword() + "%"));
        }

        if (request.getNames() != null && !request.getNames().isEmpty()) {
            var names = generalUtil.decodeToParams(request.getNames());
            spec = spec.and((root, query, cb) -> root.get("name").in(names));
        }

        return pageMapper.toPageResponse(
                permissionRepository.findAll(spec, pageable).map(permissionMapper::toPermissionResponse));
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

    public Set<PermissionResponse> findByNames(String name) throws Exception {
        var permissions = permissionRepository.findAllByNameIn(generalUtil.decodeToParams(name));
        log.info(permissions.toString());
        return permissions.stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toSet());
    }
}
