package com.example.pancharm.controller;

import java.util.List;

import com.example.pancharm.dto.request.collection.*;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CollectionDetailResponse> create(@ModelAttribute @Valid CollectionCreationRequest request) {
        return ApiResponse.<CollectionDetailResponse>builder()
                .result(collectionService.createCollection(request))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CollectionDetailResponse> update(
            @PathVariable int id, @ModelAttribute @Valid CollectionUpdateRequest request) {
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

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CollectionDetailResponse> updateCollectionImages(
            @PathVariable int id, @ModelAttribute @Valid CollectionUpdateImageRequest request) {
        return ApiResponse.<CollectionDetailResponse>builder()
                .result(collectionService.updateCollectionImage(id, request))
                .build();
    }

    @GetMapping("/default")
    public ApiResponse<List<CollectionDetailResponse>> getDefaultCollection() {
        return ApiResponse.<List<CollectionDetailResponse>>builder()
                .result(collectionService.getDefaultCollections())
                .build();
    }

    @PutMapping(value = "/{id}/update-products")
    public ApiResponse<CollectionDetailResponse> updateCollectionProducts(@PathVariable int id, @RequestBody CollectionUpdateProductRequest request) {
        return ApiResponse.<CollectionDetailResponse>builder()
                .result(collectionService.updateCollectionProducts(id, request))
                .build();
    }

    @PutMapping(value = "/{id}/remove-product")
    public ApiResponse<CollectionDetailResponse> removeProductFromCollection(@PathVariable int id, @RequestBody CollectionRemoveProductRequest request) {
        return ApiResponse.<CollectionDetailResponse>builder()
                .result(collectionService.removeProductFromCollection(id, request))
                .build();
    }
}
