package com.example.pancharm.service.product;

import com.example.pancharm.constant.ConfigurationName;
import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.constant.ProductStatus;
import com.example.pancharm.dto.request.product.ProductRequest;
import com.example.pancharm.dto.response.product.ProductResponse;
import com.example.pancharm.entity.ProductImages;
import com.example.pancharm.entity.Products;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.repository.ConfigurationRepository;
import com.example.pancharm.repository.ProductImageRepository;
import com.example.pancharm.service.base.S3Service;
import com.example.pancharm.service.configuration.ConfigurationService;
import com.example.pancharm.util.JsonConfigUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.pancharm.mapper.ProductMapper;
import com.example.pancharm.repository.CategoryRepository;
import com.example.pancharm.repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasAnyRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name(), "
        + "T(com.example.pancharm.constant.PredefineRole).ADMIN.name())")
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    ProductImageRepository productImagesRepository;
    CategoryRepository categoryRepository;
    //	CollectionRepository collectionRepository;
    S3Service s3Service;

    ConfigurationService  configurationService;

    /**
     * @desc Create new product
     * @param request
     * @return ProductResponse
     */
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Products product = productMapper.toProduct(request);
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            if (productRepository.existsBySlug(request.getSlug())) {
                throw new AppException(ErrorCode.SLUG_EXISTED);
            }
            product.setSlug(request.getSlug());
        }

        var category = categoryRepository.findById(String.valueOf(request.getCategoryId()))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        if (product.getSlug() == null || product.getSlug().isBlank()) {
            product.setSlug(generateSlug(product.getId()));
        }
        setProductImages(request, product);

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
     * @return ProductResponse
     */
    public ProductResponse updateProduct(int id, ProductRequest request) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productMapper.updateProduct(request, product);
        setProductImages(request, product);

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
    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        productRepository.deleteById(id);
    }

    /**
     * @desc Get existing product
     * @param id
     * @return ProductResponse
     */
    public ProductResponse getProduct(int id) {
        var product =  productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponse(product);
    }

    /**
     * @desc Get all products
     * @return List<ProductResponse>
     */
    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream().map(productMapper::toProductResponse).toList();
    }

    /**
     * @desc Set product image for Product
     * @param request
     * @param product
     */
    private void setProductImages(ProductRequest request, Products product) {
        Set<ProductImages> images = request.getProductImages().stream()
                .map(file -> {
                    String url = s3Service.uploadFile(file, "products/" + product.getId());
                    return ProductImages.builder()
                            .path(url)
                            .product(product)
                            .build();
                }).collect(Collectors.toSet());

        productImagesRepository.saveAll(images);
        product.setImages(images);
    }

    /**
     * @desc Generate Slug for Product
     * @param slugId
     * @return String
     */
    private String generateSlug(int slugId) {
        var defaultSlug = "PID-";
        var companyConfiguration = configurationService.getCompanyConfiguration();
        var productConfig = JsonConfigUtil.getString(companyConfiguration.getConfig(), "product");
        var productSlugConfig = (productConfig != null) ? JsonConfigUtil.getString(productConfig, "slug") : null;
        if (productSlugConfig != null && !productSlugConfig.isBlank()) {
            defaultSlug = productSlugConfig;
        }
        return defaultSlug + slugId;
    }
}
