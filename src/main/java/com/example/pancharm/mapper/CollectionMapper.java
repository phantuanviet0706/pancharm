package com.example.pancharm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.pancharm.dto.request.collection.CollectionCreationRequest;
import com.example.pancharm.dto.request.collection.CollectionUpdateRequest;
import com.example.pancharm.dto.response.collection.CollectionDetailResponse;
import com.example.pancharm.dto.response.collection.CollectionListResponse;
import com.example.pancharm.entity.Collections;

@Mapper(componentModel = "spring")
public interface CollectionMapper {
    @Mapping(target = "images", ignore = true)
    Collections toCollections(CollectionCreationRequest request);

    @Mapping(target = "collectionImages", source = "images")
    CollectionDetailResponse toCollectionDetailResponse(Collections collection);

    @Mapping(target = "collectionImages", source = "images")
    CollectionListResponse toCollectionListResponse(Collections collection);

    @Mapping(target = "images", ignore = true)
    void updateCollection(@MappingTarget Collections collection, CollectionUpdateRequest request);
}
