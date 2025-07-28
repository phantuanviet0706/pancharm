package com.example.pancharm.dto.request.product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
	String name;

	String slug;

	int quantity;

	int unitPrice;

	String color;

	String status;

	String description;

	int categoryId;

	Set<File> productImages;

}
