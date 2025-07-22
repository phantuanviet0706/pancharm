package com.example.pancharm.dto.response;

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
	int parent_id;
	String config;
	Set<ProductResponse> product_ids;
	Set<CategoryResponse> category_ids;
}
