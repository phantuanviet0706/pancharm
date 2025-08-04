package com.example.pancharm.mapper;

import com.example.pancharm.dto.response.collection.CollectionDetailResponse;
import com.example.pancharm.dto.response.collection.CollectionListResponse;
import com.example.pancharm.entity.Collections;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CollectionMapper {
	CollectionDetailResponse toCollectionDetailResponse(Collections collection);
	CollectionListResponse toCollectionListResponse(Collections collection);
}
