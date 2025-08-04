package com.example.pancharm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.pancharm.dto.request.category.CategoryRequest;
import com.example.pancharm.dto.response.category.*;
import com.example.pancharm.entity.Categories;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Categories toCategories(CategoryRequest request);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "parentCategoryId", source = "parent.id")
    @Mapping(target = "parentCategoryName", source = "parent.name")
    CategoryDetailResponse toCategoryResponse(Categories category);

    CategoryListResponse toCategoryListResponse(Categories category);

    void updateCategories(@MappingTarget Categories categories, CategoryRequest request);
}
