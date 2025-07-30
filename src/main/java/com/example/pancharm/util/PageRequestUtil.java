package com.example.pancharm.util;

import com.example.pancharm.dto.request.base.PageDefaultRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestUtil {
	public static Pageable from(PageDefaultRequest request) {
		Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortDirection())
				? Sort.Direction.ASC : Sort.Direction.DESC;

		return PageRequest.of(
				request.getPage(),
				request.getSize(),
				Sort.by(direction, request.getSortBy())
		);
	}
}
