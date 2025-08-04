package com.example.pancharm.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.pancharm.dto.request.category.CategoryFilterRequest;
import com.example.pancharm.dto.request.category.CategoryRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.category.*;
import com.example.pancharm.service.category.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @GetMapping
    public ApiResponse<PageResponse<CategoryListResponse>> getCategories(CategoryFilterRequest request) {
        return ApiResponse.<PageResponse<CategoryListResponse>>builder()
                .result(categoryService.findAll(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryDetailResponse> getCategory(@PathVariable int id) {
        return ApiResponse.<CategoryDetailResponse>builder()
                .result(categoryService.getCategory(id))
                .build();
    }

    @PostMapping
    public ApiResponse<CategoryDetailResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        return ApiResponse.<CategoryDetailResponse>builder()
                .result(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryDetailResponse> updateCategory(
            @RequestBody @Valid CategoryRequest request, @PathVariable int id) {
        return ApiResponse.<CategoryDetailResponse>builder()
                .result(categoryService.updateCategory(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder().build();
    }
}
