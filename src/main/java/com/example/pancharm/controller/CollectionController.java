package com.example.pancharm.controller;

import com.example.pancharm.dto.request.collection.CollectionFilterRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.collection.*;
import com.example.pancharm.service.collection.CollectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
