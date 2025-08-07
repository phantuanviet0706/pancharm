package com.example.pancharm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.pancharm.dto.request.product.*;
import com.example.pancharm.dto.response.product.*;
import com.example.pancharm.entity.Products;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    Products toProduct(ProductCreationRequest request);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "productImages", source = "images")
    ProductDetailResponse toProductResponse(Products product);

    @Mapping(target = "productImages", source = "images")
    ProductListResponse toProductListResponse(Products product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "slug", ignore = true)
    void updateProduct(ProductUpdateRequest request, @MappingTarget Products product);
}
