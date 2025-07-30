package com.example.pancharm.dto.request.product;

import com.example.pancharm.dto.request.base.PageDefaultRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilterRequest extends PageDefaultRequest {
    String keyword;
    String slug;
    Integer quantityFrom;
    Integer quantityTo;
    Integer unitPriceFrom;
    Integer unitPriceTo;
}
