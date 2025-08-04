package com.example.pancharm.service.collection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.pancharm.dto.request.collection.CollectionFilterRequest;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.collection.CollectionListResponse;
import com.example.pancharm.entity.Collections;
import com.example.pancharm.mapper.CollectionMapper;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.repository.CollectionRepository;
import com.example.pancharm.util.PageRequestUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CollectionService {
    CollectionRepository collectionRepository;
    CollectionMapper collectionMapper;
    PageMapper pageMapper;

    public PageResponse<CollectionListResponse> findAll(CollectionFilterRequest request) {
        Pageable pageable = PageRequestUtil.from(request);

        Specification<Collections> spec = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getKeyword() + "%")));
        }

        return pageMapper.toPageResponse(
                collectionRepository.findAll(spec, pageable).map(collectionMapper::toCollectionListResponse));
    }
}
