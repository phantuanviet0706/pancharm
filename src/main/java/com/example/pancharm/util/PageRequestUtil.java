package com.example.pancharm.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.pancharm.dto.request.base.PageDefaultRequest;

public class PageRequestUtil {
    /**
     * @desc This function is reuse for pagination & sort direction of Objects
     * @param request
     * @return
     */
    public static Pageable from(PageDefaultRequest request) {
        Sort.Direction direction =
                "asc".equalsIgnoreCase(request.getSortDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;

        return PageRequest.of(request.getPage(), request.getLimit(), Sort.by(direction, request.getSortBy()));
    }
}
