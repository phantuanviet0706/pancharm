package com.example.pancharm.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.pancharm.dto.request.permission.PermissionRequest;
import com.example.pancharm.dto.response.ApiResponse;
import com.example.pancharm.dto.response.PermissionResponse;
import com.example.pancharm.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    private PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(permissionRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<PermissionResponse> updatePermission(
            @RequestBody @Valid PermissionRequest permissionRequest, @PathVariable int id) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.updatePermission(permissionRequest, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable int id) {
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> findAllPermission() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.findAll())
                .build();
    }
}
