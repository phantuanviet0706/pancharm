package com.example.pancharm.service.collection;

import java.util.List;
import java.util.Set;

import com.example.pancharm.dto.request.collection.*;
import com.example.pancharm.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.collection.CollectionDetailResponse;
import com.example.pancharm.dto.response.collection.CollectionListResponse;
import com.example.pancharm.entity.CollectionImages;
import com.example.pancharm.entity.Collections;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.CollectionMapper;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.repository.CollectionImageRepository;
import com.example.pancharm.repository.CollectionRepository;
import com.example.pancharm.util.GeneralUtil;
import com.example.pancharm.util.ImageUtil;
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
    CollectionImageRepository collectionImageRepository;

    ImageUtil imageUtil;
    GeneralUtil generalUtil;
    ProductRepository productRepository;

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

        if (request.getIsDefault() != 0) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isDefault"), request.getIsDefault())));
        }

        return pageMapper.toPageResponse(
                collectionRepository.findAll(spec, pageable).map(collectionMapper::toCollectionListResponse));
    }

    /**
     * @desc Create new collection
     * @param request
     * @return CollectionDetailResponse
     */
    @PreAuthorize("hasAnyRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name(), "
            + "T(com.example.pancharm.constant.PredefineRole).ADMIN.name())")
    @Transactional
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

        if (request.getCollectionImages() != null
                && !request.getCollectionImages().isEmpty()) {
            imageUtil.attachImages(
                    request.getCollectionImages(),
                    collection,
                    "collections",
                    url -> CollectionImages.builder().build(),
                    collectionImageRepository::saveAll);
        }

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
    @PreAuthorize("hasAnyRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name(), "
            + "T(com.example.pancharm.constant.PredefineRole).ADMIN.name())")
    @Transactional
    public CollectionDetailResponse updateCollection(int id, CollectionUpdateRequest request) {
        var collection = collectionRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);
        });

        collectionMapper.updateCollection(collection, request);

        if (request.getCollectionImages() != null
                && !request.getCollectionImages().isEmpty()) {
            Set<CollectionImages> oldImages = collection.getImages();

            for (CollectionImages oldImage : oldImages) {
                imageUtil.deletePaths(oldImage.getPath());
            }
            collectionImageRepository.deleteAll(oldImages);

            imageUtil.attachImages(
                    request.getCollectionImages(),
                    collection,
                    "collections",
                    url -> CollectionImages.builder().build(),
                    collectionImageRepository::saveAll);
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
    @PreAuthorize("hasAnyRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name(), "
            + "T(com.example.pancharm.constant.PredefineRole).ADMIN.name())")
    @Transactional
    public void deleteCollection(int id) {
        var collection = collectionRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);
        });

        collectionImageRepository.deleteAll(collection.getImages());
        collectionRepository.delete(collection);
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

    /**
     * @desc Update default image of collection
     * @param id
     * @param request
     * @return
     */
    public CollectionDetailResponse updateCollectionImage(int id, CollectionUpdateImageRequest request) {
        var collection = collectionRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);
        });

        var collectionImages = collection.getImages();
        collectionImages.forEach(oldImage -> {
            if (oldImage.getId() == request.getDefaultImageId()) {
                oldImage.setIsDefault((short) 1);
            } else {
                oldImage.setIsDefault((short) 0);
            }
        });

        try {
            collectionImageRepository.saveAll(collectionImages);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return collectionMapper.toCollectionDetailResponse(collection);
    }

    /**
     * @desc Get all default collections to screen
     * @return
     */
    public List<CollectionDetailResponse> getDefaultCollections() {
        var collections = collectionRepository.findAllByIsDefault(Short.valueOf("1"));

        return collections.stream()
                .map(collectionMapper::toCollectionDetailResponse)
                .toList();
    }

    /**
     * @desc Update add product to collections
     * @param id
     * @param request
     * @return
     */
    public CollectionDetailResponse updateCollectionProducts(int id, CollectionUpdateProductRequest request) {
        var collection = collectionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.COLLECTION_NOT_FOUND)
        );

        var productIds = request.getProductIds();

        var products = productRepository.findAllById(productIds);
        for (int i = 0; i < products.size(); i++) {
            var product = products.get(i);
            if (product.getCollections().contains(collection)) {
                throw new AppException(ErrorCode.PRODUCT_COLLECTION_EXISTED);
            }

            product.getCollections().add(collection);
        }

        productRepository.saveAll(products);
        return collectionMapper.toCollectionDetailResponse(collection);
    }

    /**
     * @desc Remove product from collection
     * @param id
     * @param request
     * @return
     */
    public CollectionDetailResponse removeProductFromCollection(int id, CollectionRemoveProductRequest request) {
        var collection = collectionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.COLLECTION_NOT_FOUND)
        );

        var product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        if (!collection.getProducts().contains(product)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_IN_COLLECTION);
        }

        product.getCollections().remove(collection);
        collectionRepository.save(collection);
        return collectionMapper.toCollectionDetailResponse(collection);
    }
}
