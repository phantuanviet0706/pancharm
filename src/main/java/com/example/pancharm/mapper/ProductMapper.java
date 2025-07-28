package com.example.pancharm.mapper;

import com.example.pancharm.dto.request.product.ProductRequest;
import com.example.pancharm.dto.response.product.ProductResponse;
import com.example.pancharm.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	@Mapping(target = "category", ignore = true)
	@Mapping(target = "images", ignore = true)
	Products toProduct(ProductRequest request);

	ProductResponse toProductResponse(Products product);

	@Mapping(target = "category", ignore = true)
	@Mapping(target = "images", ignore = true)
	@Mapping(target = "slug", ignore = true)
	void updateProduct(ProductRequest request, @MappingTarget Products product);
}
