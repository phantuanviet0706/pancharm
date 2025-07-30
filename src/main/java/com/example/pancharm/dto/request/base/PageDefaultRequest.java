package com.example.pancharm.dto.request.base;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageDefaultRequest {
	int page = 0;
	int size = 50;
	String sortBy = "id";
	String sortDirection = "asc";
}
