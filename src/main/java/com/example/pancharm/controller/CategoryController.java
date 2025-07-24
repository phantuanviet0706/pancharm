package com.example.pancharm.controller;

import com.example.pancharm.constant.PredefineRole;
import com.example.pancharm.dto.request.category.CategoryRequest;
import com.example.pancharm.dto.response.ApiResponse;
import com.example.pancharm.dto.response.CategoryResponse;
import com.example.pancharm.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasAnyRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name(), " +
		"T(com.example.pancharm.constant.PredefineRole).ADMIN.name())")
public class CategoryController {
	CategoryService categoryService;

	@GetMapping
	public ApiResponse<List<CategoryResponse>> getCategories() {
		return ApiResponse.<List<CategoryResponse>>builder()
				.result(categoryService.findAll())
				.build();
	}

	@GetMapping("/{id}")
	public ApiResponse<CategoryResponse> getCategory(@PathVariable int id) {
		return ApiResponse.<CategoryResponse>builder()
				.result(categoryService.getCategory(id))
				.build();
	}

	@PostMapping
	public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
		return ApiResponse.<CategoryResponse>builder()
				.result(categoryService.createCategory(request))
				.build();
	}

	@PutMapping("/{id}")
	public ApiResponse<CategoryResponse> updateCategory(@RequestBody @Valid CategoryRequest request, @PathVariable int id) {
		return ApiResponse.<CategoryResponse>builder()
				.result(categoryService.updateCategory(id, request))
				.build();
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> deleteCategory(@PathVariable int id) {
		categoryService.deleteCategory(id);
		return ApiResponse.<Void>builder().build();
	}
}
