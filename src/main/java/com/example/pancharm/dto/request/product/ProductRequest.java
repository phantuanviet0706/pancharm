package com.example.pancharm.dto.request.product;

import java.util.Set;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @NotBlank(message = "PRODUCT_NAME_REQUIRE")
    String name;

    String slug;

    @NotNull(message = "PRODUCT_QUANTITY_REQUIRE")
    @Min(value = 1, message = "PRODUCT_QUANTITY_MINIMUM")
    Integer quantity;

    @Min(value = 0, message = "PRODUCT_UNIT_PRICE_REQUIRE")
    int unitPrice;

    String color;

    String status;

    String description;

    int categoryId;

    Set<MultipartFile> productImages;
}
