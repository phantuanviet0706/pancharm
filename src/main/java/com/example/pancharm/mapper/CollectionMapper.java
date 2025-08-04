package com.example.pancharm.mapper;

import com.example.pancharm.dto.request.collection.CollectionCreationRequest;
import com.example.pancharm.dto.request.collection.CollectionUpdateRequest;
import org.mapstruct.Mapper;

import com.example.pancharm.dto.response.collection.CollectionDetailResponse;
import com.example.pancharm.dto.response.collection.CollectionListResponse;
import com.example.pancharm.entity.Collections;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CollectionMapper {
	@Mapping(target = "images", ignore = true)
	Collections toCollections(CollectionCreationRequest request);

    CollectionDetailResponse toCollectionDetailResponse(Collections collection);

    CollectionListResponse toCollectionListResponse(Collections collection);

	@Mapping(target = "images", ignore = true)
	void updateCollection(@MappingTarget Collections collection, CollectionUpdateRequest request);
}
