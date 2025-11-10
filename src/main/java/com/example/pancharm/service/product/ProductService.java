package com.example.pancharm.service.product;

import java.util.Set;
import java.util.stream.Collectors;

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
            productRepository.save(product);
        } catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        if (product.getSlug() == null || product.getSlug().isBlank()) {
            product.setSlug(generalUtil.generateSlug("product", product.getId()));
        }

        if (request.getProductImages() != null && !request.getProductImages().isEmpty()) {
            imageUtil.attachImages(
                    request.getProductImages(),
                    product,
                    "products",
                    url -> ProductImages.builder().build(),
                    productImagesRepository::saveAll);
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

        Set<ProductImages> oldImages = product.getImages();

        imageUtil.attachImages(
                request.getProductImages(),
                product,
                "products",
                url -> ProductImages.builder().build(),
                productImagesRepository::saveAll);

        Set<String> newImagePaths =
                product.getImages().stream().map(ProductImages::getPath).collect(Collectors.toSet());

        for (ProductImages oldImage : oldImages) {
            if (!newImagePaths.contains(oldImage.getPath())) {
                imageUtil.deletePaths(oldImage.getPath());
            }
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

        product.setSoftDeleted((short) 1);
        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

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

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("softDeleted").as(Boolean.class), false));

        return pageMapper.toPageResponse(
                productRepository.findAll(spec, pageable).map(productMapper::toProductListResponse));
    }
}
