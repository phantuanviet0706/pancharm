package com.example.pancharm.controller;

import com.example.pancharm.dto.request.role.RoleRequest;
import com.example.pancharm.dto.response.ApiResponse;
import com.example.pancharm.dto.response.RoleResponse;
import com.example.pancharm.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	public ApiResponse<List<RoleResponse>> getRoles() {
		return ApiResponse.<List<RoleResponse>>builder()
				.result(roleService.findAll())
				.build();
	}
}
