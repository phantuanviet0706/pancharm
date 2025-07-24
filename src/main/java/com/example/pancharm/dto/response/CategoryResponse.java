package com.example.pancharm.dto.response;

import com.example.pancharm.entity.Categories;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
	String name;
	String slug;
	int parentCategoryId;
	String parentCategoryName;
	String config;
	Set<ProductResponse> products;
	Set<CategoryResponse> categories;
}
