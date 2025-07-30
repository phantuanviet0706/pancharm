package com.example.pancharm.controller;

import java.util.List;

import com.example.pancharm.dto.request.role.RoleFilterRequest;
import com.example.pancharm.dto.response.base.PageResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.pancharm.dto.request.role.RoleRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.role.RoleResponse;
import com.example.pancharm.service.role.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    private RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(roleRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> updateRole(@RequestBody @Valid RoleRequest roleRequest, @PathVariable int id) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRole(roleRequest, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable int id) {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping
    public ApiResponse<PageResponse<RoleResponse>> getRoles(RoleFilterRequest request) {
        return ApiResponse.<PageResponse<RoleResponse>>builder()
                .result(roleService.findAll(request))
                .build();
    }
}
