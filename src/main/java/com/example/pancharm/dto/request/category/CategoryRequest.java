package com.example.pancharm.dto.request.category;

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
public class CategoryRequest {
	String name;
	String slug;
	int parent_id;
	String config;
	Set<Integer> product_ids;
	Set<Integer> category_ids;
}
