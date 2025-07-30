package com.example.pancharm.controller;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.pancharm.dto.request.product.ProductFilterRequest;
import com.example.pancharm.dto.request.product.ProductRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.product.ProductResponse;
import com.example.pancharm.service.product.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> createProduct(@ModelAttribute @Valid ProductRequest productRequest) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(productRequest))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> updateProduct(
            @ModelAttribute @Valid ProductRequest productRequest, @PathVariable int id) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id, productRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductResponse>> getProducts(ProductFilterRequest request) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productService.getProducts(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable int id) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProduct(id))
                .build();
    }
}
