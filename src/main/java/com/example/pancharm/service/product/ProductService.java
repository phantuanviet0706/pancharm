package com.example.pancharm.service.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.example.pancharm.constant.FileConstants;
import com.example.pancharm.service.common.EditorImageService;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.product.*;
import com.example.pancharm.dto.request.product.ProductFilterRequest;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.product.*;
import com.example.pancharm.entity.ProductImages;
import com.example.pancharm.entity.Products;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.mapper.ProductMapper;
import com.example.pancharm.repository.CategoryRepository;
import com.example.pancharm.repository.ProductImageRepository;
import com.example.pancharm.repository.ProductRepository;
import com.example.pancharm.util.GeneralUtil;
import com.example.pancharm.util.ImageUtil;
import com.example.pancharm.util.PageRequestUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    ProductImageRepository productImagesRepository;
    CategoryRepository categoryRepository;
    EditorImageService editorImageService;

    PageMapper pageMapper;
    ImageUtil imageUtil;
    GeneralUtil generalUtil;

    /**
     * @desc Create new product
     * @param request
     * @return ProductDetailResponse
     */
    @PreAuthorize("hasAnyRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name(), "
            + "T(com.example.pancharm.constant.PredefineRole).ADMIN.name())")
    @Transactional
    public ProductDetailResponse createProduct(ProductCreationRequest request) {
        Products product = productMapper.toProduct(request);
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            if (productRepository.existsBySlugAndSoftDeleted(request.getSlug(), (short) 0)) {
                throw new AppException(ErrorCode.SLUG_EXISTED);
            }
            product.setSlug(request.getSlug());
        }

        var category = categoryRepository
                .findById(String.valueOf(request.getCategoryId()))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        try {
            product = productRepository.save(product);
        } catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        if (product.getSlug() == null || product.getSlug().isBlank()) {
            product.setSlug(generalUtil.generateSlug("product", product.getId()));
        }

        String finalHtml = editorImageService.moveDraftImagesAndRewriteHtml(
                request.getDescription(),
                request.getDraftId(),
                product.getId()
        );

        product.setDescription(finalHtml);

        if (request.getProductImages() != null && !request.getProductImages().isEmpty()) {
            imageUtil.attachImages(
                    request.getProductImages(),
                    product,
                    "products",
                    url -> ProductImages.builder().build(),
                    productImagesRepository::saveAll,
                    FileConstants.IMAGE_EXTENSION);
        }

        try {
            product = productRepository.save(product);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return productMapper.toProductResponse(product);
    }

    /**
     * @desc Update existing product's information
     * @param id
     * @param request
     * @return ProductDetailResponse
     */
    @PreAuthorize("hasAnyRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name(), "
            + "T(com.example.pancharm.constant.PredefineRole).ADMIN.name())")
    @Transactional
    public ProductDetailResponse updateProduct(int id, ProductUpdateRequest request) {
        Products product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productMapper.updateProduct(request, product);

        String finalHtml = editorImageService.moveDraftImagesAndRewriteHtml(
                request.getDescription(),
                request.getDraftId(),
                product.getId()
        );
        product.setDescription(finalHtml);

        if (request.getProductImages() != null && !request.getProductImages().isEmpty()) {
            Set<ProductImages> oldImages = product.getImages();

            for (ProductImages oldImage : oldImages) {
                imageUtil.deletePaths(oldImage.getPath());
            }
            productImagesRepository.deleteAll(oldImages);

            imageUtil.attachImages(
                    request.getProductImages(),
                    product,
                    "products",
                    url -> ProductImages.builder().build(),
                    productImagesRepository::saveAll,
                    FileConstants.IMAGE_EXTENSION);
        }

        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return productMapper.toProductResponse(product);
    }

    /**
     * @desc Delete existing product
     * @param id
     */
    @PreAuthorize("hasAnyRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name(), "
            + "T(com.example.pancharm.constant.PredefineRole).ADMIN.name())")
    @Transactional
    public void deleteProduct(int id) {
        var product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        //        product.setSoftDeleted((short) 1);
        //        try {
        //            productRepository.save(product);
        //        } catch (DataIntegrityViolationException exception) {
        //            throw new AppException(ErrorCode.UPDATE_ERROR);
        //        }

        productImagesRepository.deleteAll(product.getImages());
        productRepository.deleteById(id);
    }

    /**
     * @desc Get existing product
     * @param id
     * @return ProductDetailResponse
     */
    public ProductDetailResponse getProduct(int id) {
        var product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponse(product);
    }

    /**
     * @desc Get all products
     * @param request
     * @return List<ProductListResponse>
     */
    public PageResponse<ProductListResponse> getProducts(ProductFilterRequest request) {
        Pageable pageable = PageRequestUtil.from(request);

        Specification<Products> spec = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (request.getIds() != null && !request.getIds().isBlank()) {
            var ids = Arrays.stream(request.getIds().split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .toList();
            spec = spec.and((root, query, cb) -> root.get("id").in(ids));
        }

        if (request.getCategoryId() != null) {
            spec = spec.and((root, query, cb) -> root.get("category").in(request.getCategoryId()));
        }

        if (request.getCollectionId() != null) {
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                var collectionJoin = root.join("collections", JoinType.INNER);
                return cb.equal(collectionJoin.get("id"), request.getCollectionId());
            });
        }

        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("slug").as(String.class), "%" + request.getSlug() + "%"));
        }

        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getKeyword() + "%"));
        }

        if (request.getQuantityFrom() != null
                && request.getQuantityTo() != null
                && request.getQuantityFrom() >= 0
                && request.getQuantityTo() >= 0) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.between(
                    root.get("quantityFrom"), request.getQuantityFrom(), request.getQuantityTo()));
        } else if (request.getQuantityFrom() != null && request.getQuantityFrom() >= 0) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("quantityFrom"), request.getQuantityFrom()));
        } else if (request.getQuantityTo() != null && request.getQuantityTo() >= 0) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("quantityTo"), request.getQuantityTo()));
        }

        if (request.getUnitPriceFrom() != null
                && request.getUnitPriceTo() != null
                && request.getUnitPriceFrom() >= 0
                && request.getUnitPriceTo() >= 0) {
            spec = spec.and((root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(root.get("unitPrice"), request.getUnitPriceFrom()))
                    .and((root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(root.get("unitPrice"), request.getUnitPriceTo()));
        } else if (request.getUnitPriceFrom() != null && request.getUnitPriceFrom() >= 0) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("unitPrice"), request.getUnitPriceFrom()));
        } else if (request.getUnitPriceTo() != null && request.getUnitPriceTo() >= 0) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("unitPrice"), request.getUnitPriceTo()));
        }

        if (request.getPriceRanges() != null && !request.getPriceRanges().isBlank()) {
            spec = spec.and((root, query, cb) -> {
                String[] ranges = request.getPriceRanges().split(",");
                List<Predicate> orPredicates = new ArrayList<>();

                for (String range : ranges) {
                    String[] prices = range.split("-");

                    long min = prices.length > 0 && prices[0] != null && !prices[0].isBlank()
                            ? Long.parseLong(prices[0])
                            : 0;

                    long max = prices.length > 1 && prices[1] != null && !prices[1].isBlank()
                            ? Long.parseLong(prices[1])
                            : Long.MAX_VALUE;

                    orPredicates.add(cb.between(root.get("unitPrice"), min, max));
                }

                return cb.or(orPredicates.toArray(new Predicate[0]));
            });
        }

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("softDeleted").as(Boolean.class), false));

        return pageMapper.toPageResponse(
                productRepository.findAll(spec, pageable).map(productMapper::toProductListResponse));
    }

    /**
     * @desc Update default image of product
     * @param id
     * @param request
     * @return
     */
    public ProductDetailResponse updateImage(int id, ProductUpdateImageRequest request) {
        var product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        var productImages = product.getImages();
        productImages.forEach(oldImage -> {
            if (oldImage.getId() == request.getDefaultImageId()) {
                oldImage.setIsDefault((short) 1);
            } else {
                oldImage.setIsDefault((short) 0);
            }
        });

        try {
            productImagesRepository.saveAll(productImages);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return productMapper.toProductResponse(product);
    }
}
