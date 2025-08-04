package com.example.pancharm.controller;

import com.example.pancharm.dto.request.collection.CollectionCreationRequest;
import com.example.pancharm.dto.request.collection.CollectionUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.example.pancharm.dto.request.collection.CollectionFilterRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.collection.*;
import com.example.pancharm.service.collection.CollectionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CollectionController {
    CollectionService collectionService;

    @GetMapping
    public ApiResponse<PageResponse<CollectionListResponse>> findAll(CollectionFilterRequest request) {
        return ApiResponse.<PageResponse<CollectionListResponse>>builder()
                .result(collectionService.findAll(request))
                .build();
    }

    @PostMapping
    public ApiResponse<CollectionDetailResponse> create(@ModelAttribute @Valid CollectionCreationRequest request) {
        return ApiResponse.<CollectionDetailResponse>builder()
                .result(collectionService.createCollection(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CollectionDetailResponse> update(@PathVariable int id, @ModelAttribute @Valid CollectionUpdateRequest request) {
        return ApiResponse.<CollectionDetailResponse>builder()
                .result(collectionService.updateCollection(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        collectionService.deleteCollection(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CollectionDetailResponse> findById(@PathVariable int id) {
        return ApiResponse.<CollectionDetailResponse>builder()
                .result(collectionService.getById(id))
                .build();
    }
}
