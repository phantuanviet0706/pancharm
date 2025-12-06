package com.example.pancharm.dto.request.product;

import com.example.pancharm.validator.annotation.UnitPriceConstraint;
import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest extends ProductImageRequest {
    @NotBlank(message = "PRODUCT_NAME_REQUIRE")
    @Size(max = 255, message = "PRODUCT_NAME_MAX_255")
    String name;

    @Size(max = 63, message = "PRODUCT_SLUG_MAX_63")
    String slug;

    //    @NotNull(message = "PRODUCT_QUANTITY_REQUIRE")
    //    @Min(value = 1, message = "PRODUCT_QUANTITY_MINIMUM")
    //    Integer quantity;

    @NotNull(message = "PRODUCT_UNIT_PRICE_REQUIRED")
    @Min(value = 0, message = "PRODUCT_UNIT_PRICE_REQUIRED")
    @UnitPriceConstraint(message = "PRODUCT_UNIT_PRICE_REQUIRED")
    Integer unitPrice;

    String color;

    String status;

    String description;

    @NotNull(message = "PRODUCT_CATEGORY_REQUIRED")
    Integer categoryId;

    String draftId;
}
