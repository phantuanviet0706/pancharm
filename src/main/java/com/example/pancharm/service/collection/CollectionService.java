package com.example.pancharm.service.collection;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.collection.CollectionCreationRequest;
import com.example.pancharm.dto.request.collection.CollectionUpdateRequest;
import com.example.pancharm.dto.response.collection.CollectionDetailResponse;
import com.example.pancharm.entity.CollectionImages;
import com.example.pancharm.entity.ProductImages;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.repository.CollectionImageRepository;
import com.example.pancharm.util.GeneralUtil;
import com.example.pancharm.util.ImageUtil;
import org.springframework.dao.DataIntegrityViolationException;
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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CollectionService {
    CollectionRepository collectionRepository;
    CollectionMapper collectionMapper;
    PageMapper pageMapper;
	CollectionImageRepository collectionImageRepository;

	ImageUtil imageUtil;
	GeneralUtil generalUtil;

	/**
	 * @desc Get all Collections
	 * @param request
	 * @return PageResponse<CollectionListResponse>
	 */
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

	/**
	 * @desc Create new collection
	 * @param request
	 * @return CollectionDetailResponse
	 */
	public CollectionDetailResponse createCollection(CollectionCreationRequest request) {
		Collections collection = collectionMapper.toCollections(request);
		if (request.getSlug() == null || request.getSlug().isBlank()) {
			collection.setSlug("");
		} else if (collectionRepository.existsBySlug(request.getSlug())) {
			throw new AppException(ErrorCode.SLUG_EXISTED);
		} else {
			collection.setSlug(request.getSlug());
		}

		try {
			collection = collectionRepository.save(collection);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		if (collection.getSlug() == null || collection.getSlug().isBlank()) {
			collection.setSlug(generalUtil.generateSlug("collection", collection.getId()));
		}

		imageUtil.attachImages(
				request.getCollectionImages(),
				collection,
				"collections",
				url -> CollectionImages.builder().build(),
				collectionImageRepository::saveAll
		);

		try {
			collection = collectionRepository.save(collection);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		return collectionMapper.toCollectionDetailResponse(collection);
	}

	/**
	 * @desc Update existing collection
	 * @param id
	 * @param request
	 * @return CollectionDetailResponse
	 */
	public CollectionDetailResponse updateCollection(int id, CollectionUpdateRequest request) {
		var collection = collectionRepository.findById(id).orElseThrow(() -> {
			throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);
		});

		collectionMapper.updateCollection(collection, request);

		Set<CollectionImages> oldImages = collection.getImages();

		imageUtil.attachImages(
				request.getCollectionImages(),
				collection,
				"collections",
				url -> CollectionImages.builder().build(),
				collectionImageRepository::saveAll
		);

		Set<String> newImagePaths = collection.getImages().stream()
				.map(CollectionImages::getPath)
				.collect(Collectors.toSet());

		for (CollectionImages oldImage: oldImages) {
			if (!newImagePaths.contains(oldImage.getPath())) {
				imageUtil.deletePaths(oldImage.getPath());
			}
		}

		try {
			collection = collectionRepository.save(collection);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		return collectionMapper.toCollectionDetailResponse(collection);
	}

	/**
	 * @desc Delete existing collection
	 * @param id
	 */
	public void deleteCollection(int id) {
		if (!collectionRepository.existsById(id)) {
			throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);
		}

		collectionRepository.deleteById(id);
	}

	/**
	 * @desc Get single collection
	 * @param id
	 * @return CollectionDetailResponse
	 */
	public CollectionDetailResponse getById(int id) {
		var collection = collectionRepository.findById(id).orElseThrow(() -> {
			throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);
		});

		return collectionMapper.toCollectionDetailResponse(collection);
	}
}
