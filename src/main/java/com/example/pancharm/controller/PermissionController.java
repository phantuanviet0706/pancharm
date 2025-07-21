package com.example.pancharm.controller;

import com.example.pancharm.dto.request.permission.PermissionRequest;
import com.example.pancharm.dto.response.ApiResponse;
import com.example.pancharm.dto.response.PermissionResponse;
import com.example.pancharm.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
	private PermissionService permissionService;

	@PostMapping
	public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest) {
		return ApiResponse.<PermissionResponse>builder()
				.result(permissionService.createPermission(permissionRequest))
				.build();
	}

	@PutMapping("/{id}")
	public ApiResponse<PermissionResponse> updatePermission(
			@RequestBody PermissionRequest permissionRequest,
			@PathVariable int id
	) {
		return ApiResponse.<PermissionResponse>builder()
				.result(permissionService.updatePermission(permissionRequest, id))
				.build();
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> deletePermission(int permissionId) {
		permissionService.deletePermission(permissionId);
		return ApiResponse.<Void>builder()
				.build();
	}

	@GetMapping
	public ApiResponse<List<PermissionResponse>> findAllPermission() {
		return ApiResponse.<List<PermissionResponse>>builder()
				.result(permissionService.findAll())
				.build();
	}
}
