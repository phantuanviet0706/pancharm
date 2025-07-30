package com.example.pancharm.mapper;

import com.example.pancharm.dto.response.base.PageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageMapper {
	default <T> PageResponse<T> toPageResponse(Page<T> page) {
		return PageResponse.<T>builder()
				.content(page.getContent())
				.page(page.getNumber())
				.size(page.getSize())
				.totalElements(page.getTotalElements())
				.totalPages(page.getTotalPages())
				.first(page.isFirst())
				.last(page.isLast())
				.build();
	}
}
